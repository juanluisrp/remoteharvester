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
import net.geocat.xml.XmlCapabilitiesDocument;
import net.geocat.xml.XmlDocumentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class LinkProcessor_ProcessCapDoc implements ILinkProcessor {

    @Autowired
    BlobStorageService blobStorageService;

    @Autowired
    XmlDocumentFactory xmlDocumentFactory;

    @Override
    public Link process(Link link) throws Exception {

        String sha2 = link.getLinkContentSHA2();
        if ((sha2 == null) || (sha2.isEmpty()))
            return link;

        String doc = blobStorageService.findXML(sha2);
        if ((doc == null) || (doc.isEmpty()))
            throw new Exception("couldnt load cap document from blob storage!");

        XmlCapabilitiesDocument xml = (XmlCapabilitiesDocument) xmlDocumentFactory.create(doc);

        if (xml.isHasExtendedCapabilities()) {
            link.setIndicator_HasExtendedCapabilities(IndicatorStatus.PASS);
        } else {
            link.setIndicator_HasExtendedCapabilities(IndicatorStatus.FAIL);
            return link;
        }

        String metadataUrl = xml.getMetadataUrlRaw();
        if ((metadataUrl == null) || (metadataUrl.isEmpty())) {
            link.setIndicator_HasServiceMetadataLink(IndicatorStatus.FAIL);
            return link;
        }

        link.setIndicator_HasServiceMetadataLink(IndicatorStatus.PASS);
        link.setServiceMetadataLinkURL(metadataUrl);

        return link;
    }
}
