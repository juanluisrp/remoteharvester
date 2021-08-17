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


import net.geocat.database.linkchecker.entities2.Link;
import net.geocat.service.helper.NotServiceRecordException;
import net.geocat.xml.XmlDoc;
import net.geocat.xml.XmlDocumentFactory;
import net.geocat.xml.XmlMetadataDocument;
import net.geocat.xml.XmlServiceRecordDoc;
import net.geocat.xml.helpers.OnlineResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
public class ServiceDocLinkExtractor {

    @Autowired
    XmlDocumentFactory xmlDocumentFactory;

    @Autowired
    LinkFactory linkFactory;

    public List<Link> extractLinks(String xml,
                                   String sha2,
                                   String harvestId,
                                   long endpointId,
                                   String linkCheckJobId) throws Exception {
        XmlDoc doc = xmlDocumentFactory.create(xml);
        if (doc instanceof XmlServiceRecordDoc)
            return extractLinks((XmlServiceRecordDoc) doc, sha2, harvestId, endpointId, linkCheckJobId);
        throw new NotServiceRecordException("trying to extract links from a non-service record");
    }

    public List<Link> extractLinks(XmlServiceRecordDoc xml,
                                   String sha2,
                                   String harvestId,
                                   long endpointId,
                                   String linkCheckJobId) throws Exception {
        List<Link> result = new ArrayList<>();

        List<OnlineResource> docLinks = removeDuplicates(xml.getConnectPoints(), xml.getTransferOptions());

        for (OnlineResource onlineResource : docLinks) {
            Link link = linkFactory.create(onlineResource, xml, sha2, harvestId, endpointId, linkCheckJobId);
            result.add(link);
        }
        return result;
    }


    public List<OnlineResource> extractOnlineResource(XmlMetadataDocument xml) throws Exception {

        List<OnlineResource> docLinks = removeDuplicates(xml.getConnectPoints(), xml.getTransferOptions());
        return docLinks;
    }

    public boolean badURL(String rawUrl) {
        if ((rawUrl == null) || (rawUrl.isEmpty()))
            return true;
        try {
            URL url = new URL(rawUrl);
            String protocol = url.getProtocol().toLowerCase();
            if (protocol == null)
                return true;
            if (!protocol.equals("http") && (!protocol.equals("https")))
                return true;
            URI uri = new URL(rawUrl).toURI(); //should throw if invalid
            return false;
        } catch (Exception e) {
            return false;
        }

    }

    //we can preference of connectPoints since they might have an operation's name
    // note - could have duplicates inside the individual lists, but more likely between them
    public List<OnlineResource> removeDuplicates(List<OnlineResource> connectPoints,
                                                 List<OnlineResource> transferOptions) throws Exception {
        List<OnlineResource> result = new ArrayList<>();
        for (OnlineResource onlineResource : connectPoints) {
            if (badURL(onlineResource.getRawURL()))
                continue;
            if (!inList(onlineResource, result))
                result.add(onlineResource);
        }
        for (OnlineResource onlineResource : transferOptions) {
            if (badURL(onlineResource.getRawURL()))
                continue;
            if (!inList(onlineResource, result))
                result.add(onlineResource);
        }
        return result;
    }

    public boolean inList(OnlineResource onlineResource, List<OnlineResource> list) {
        for (OnlineResource or : list) {
            if (onlineResource.getRawURL().equals(or.getRawURL()))
                return true;
        }
        return false;
    }

}
