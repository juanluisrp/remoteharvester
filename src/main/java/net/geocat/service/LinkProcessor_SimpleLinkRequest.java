/*
 *  =============================================================================
 *  ===  Copyright (C) 2021 Food and Agriculture Organization of the
 *  ===  United Nations (FAO-UN), United Nations World Food Programme (WFP)
 *  ===  and United Nations Environment Programme (UNEP)
 *  ===
 *  ===  This program is free software; you can redistribute it and/or modify
 *  ===  it under the terms of the GNU General Public License as published by
 *  ===  the Free Software Foundation; either version 2 of the License, or (at
 *  ===  your option) any later version.
 *  ===
 *  ===  This program is distributed in the hope that it will be useful, but
 *  ===  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  ===  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  ===  General Public License for more details.
 *  ===
 *  ===  You should have received a copy of the GNU General Public License
 *  ===  along with this program; if not, write to the Free Software
 *  ===  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 *  ===
 *  ===  Contact: Jeroen Ticheler - FAO - Viale delle Terme di Caracalla 2,
 *  ===  Rome - Italy. email: geonetwork@osgeo.org
 *  ===
 *  ===  Development of this program was financed by the European Union within
 *  ===  Service Contract NUMBER – 941143 – IPR – 2021 with subject matter
 *  ===  "Facilitating a sustainable evolution and maintenance of the INSPIRE
 *  ===  Geoportal", performed in the period 2021-2023.
 *  ===
 *  ===  Contact: JRC Unit B.6 Digital Economy, Via Enrico Fermi 2749,
 *  ===  21027 Ispra, Italy. email: JRC-INSPIRE-SUPPORT@ec.europa.eu
 *  ==============================================================================
 */

package net.geocat.service;

import net.geocat.database.linkchecker.entities2.IndicatorStatus;
import net.geocat.database.linkchecker.entities2.Link;
import net.geocat.http.HttpResult;
import net.geocat.http.IHTTPRetriever;
import net.geocat.service.downloadhelpers.CapabilitiesContinueReadingPredicate;
import net.geocat.xml.XmlDoc;
import net.geocat.xml.helpers.CapabilitiesType;
import net.geocat.xml.helpers.CapabilityDeterminer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class LinkProcessor_SimpleLinkRequest implements ILinkProcessor {

    @Autowired
    @Qualifier("cookieAttachingRetriever")
    IHTTPRetriever retriever;

    @Autowired
    CapabilitiesContinueReadingPredicate capabilitiesContinueReadingPredicate;

    @Autowired
    CapabilityDeterminer capabilityDeterminer;

    @Autowired
    BlobStorageService blobStorageService;

//    @Autowired
//    LinkRepo linkRepo;

    public Link process(Link link) throws Exception {
        HttpResult data = null;
        try {
            data = retriever.retrieveXML("GET", link.getFixedLinkURL(), null, null, capabilitiesContinueReadingPredicate);
        } catch (Exception e) {
            link.setIndicator_LinkResolves(IndicatorStatus.FAIL);
            link.setLinkHTTPException(e.getClass().getSimpleName() + " - " + e.getMessage());
            return link;
        }
        if ((data.getHttpCode() == 200))
            link.setIndicator_LinkResolves(IndicatorStatus.PASS);
        else
            link.setIndicator_LinkResolves(IndicatorStatus.FAIL);


        link.setLinkHTTPStatusCode(data.getHttpCode());
        link.setLinkMIMEType(data.getContentType());
        link.setActualLinkURL(data.getFinalURL());
        link.setLinkIsHTTS(data.isHTTPS());
        if (data.isHTTPS()) {
            link.setLinkSSLTrustedByJava(data.isSslTrusted());
            link.setLinkSSLUntrustedByJavaReason(data.getSslUnTrustedReason());
        }

        byte[] headData = Arrays.copyOf(data.getData(), Math.min(1000, data.getData().length));
        link.setLinkContentHead(headData);

        link.setLinkIsXML(isXML(data));

        if (!data.isFullyRead() || data.isErrorOccurred())
            return link;

        if (link.getLinkIsXML()) {
            link.setLinkCapabilitiesType(determineCapabilityType(data));
        }

        if (link.getLinkCapabilitiesType() != null) {
            String doc = new String(data.getData());
            String sha2 = blobStorageService.computeSHA2(doc);
            link.setLinkContentSHA2(sha2);
            blobStorageService.ensureBlobExists(doc, sha2);
            link.setIndicator_CapabilitiesResolves(IndicatorStatus.PASS);
            link.setIndicator_DetectProtocol(IndicatorStatus.PASS);
        }

        return link;
    }

    public CapabilitiesType determineCapabilityType(HttpResult result) {
        try {
            String doc = new String(result.getData());
            XmlDoc xmlDoc = new XmlDoc(doc);
            return capabilityDeterminer.determineCapabilitiesType(xmlDoc);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isXML(HttpResult result) {
        try {
            return capabilitiesContinueReadingPredicate.isXML(new String(result.getData()));
        } catch (Exception e) {
            return false;
        }
    }
}
