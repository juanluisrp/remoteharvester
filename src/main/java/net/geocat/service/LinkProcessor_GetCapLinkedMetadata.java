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
import net.geocat.xml.XmlDocumentFactory;
import net.geocat.xml.XmlMetadataDocument;
import net.geocat.xml.XmlServiceRecordDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.Difference;

import java.util.ArrayList;
import java.util.List;

@Component
public class LinkProcessor_GetCapLinkedMetadata implements ILinkProcessor {

    @Autowired
    @Qualifier("cookieAttachingRetriever")
    IHTTPRetriever retriever;

    @Autowired
    CapabilitiesContinueReadingPredicate capabilitiesContinueReadingPredicate;

    @Autowired
    XmlDocumentFactory xmlDocumentFactory;

    @Autowired
    BlobStorageService blobStorageService;

    public static void stripEmptyElements(Node node) {
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.TEXT_NODE) {
                if (child.getTextContent().trim().length() == 0) {
                    child.getParentNode().removeChild(child);
                    i--;
                }
            }
            stripEmptyElements(child);
        }
    }

    @Override
    public Link process(Link link) throws Exception {
        if ((link.getServiceMetadataLinkURL() == null) || (link.getServiceMetadataLinkURL().isEmpty()))
            return link; //nothing to do


        HttpResult data = null;
        try {
            data = retriever.retrieveXML("GET", link.getServiceMetadataLinkURL(), null, null, null);
        } catch (Exception e) {
            link.setIndicator_ResolveServiceMetadataLink(IndicatorStatus.FAIL);
            link.setResolveServiceMetadataLinkException(e.getClass().getSimpleName() + " - " + e.getMessage());
            return link;
        }


        if ((data.getHttpCode() != 200)) {
            link.setIndicator_ResolveServiceMetadataLink(IndicatorStatus.FAIL);
            return link;
        }

        link.setIndicator_ResolveServiceMetadataLink(IndicatorStatus.PASS);

        if (!isXML(data)) {
            link.setIndicator_MetadataLinkIsXML(IndicatorStatus.FAIL);
            return link;
        }
        link.setIndicator_MetadataLinkIsXML(IndicatorStatus.PASS);

        XmlDoc xmlDoc = xmlDocumentFactory.create(new String(data.getData()));
        if (!(xmlDoc instanceof XmlMetadataDocument)) {
            link.setIndicator_MetadataLinkIsMD_METADATA(IndicatorStatus.FAIL);
            return link;
        }
        link.setIndicator_MetadataLinkIsMD_METADATA(IndicatorStatus.PASS);

        XmlMetadataDocument xmlServiceRecordDoc = (XmlMetadataDocument) xmlDoc;
        link.setMetadataLinkMetadataType(xmlServiceRecordDoc.getMetadataDocumentType());


        if (!(xmlDoc instanceof XmlServiceRecordDoc)) {
            link.setIndicator_MetadataLinkIsServiceRecord(IndicatorStatus.FAIL);
            return link;
        }
        link.setIndicator_MetadataLinkIsServiceRecord(IndicatorStatus.PASS);

        link.setMetadataLinkFileIdentifier(xmlServiceRecordDoc.getFileIdentifier());

        if (!xmlServiceRecordDoc.getFileIdentifier().equals(link.getOriginatingServiceRecordFileIdentifier())) {
            link.setIndicator_CompareServiceMetadataLink_FileIdentifier(IndicatorStatus.FAIL);
            return link;
        }
        link.setIndicator_CompareServiceMetadataLink_FileIdentifier(IndicatorStatus.PASS);


        String xml_original = blobStorageService.findXML(link.getOriginatingServiceRecordSHA2());
        String xml_remote = XmlDoc.writeXML(xmlServiceRecordDoc.getParsedXml());
        List<Difference> diffs = areSame(xml_original, xml_remote);


        if (!diffs.isEmpty()) {
            link.setIndicator_CompareServiceMetadataLink_Full(IndicatorStatus.FAIL);
            String fullDiff = diffs.toString();
            link.setMetadataRecordDifferences(fullDiff.substring(0, Math.min(2000, fullDiff.length())));
            return link;
        }
        link.setIndicator_CompareServiceMetadataLink_Full(IndicatorStatus.PASS);


        return link;
    }

    private List<Difference> areSame(String xml_original, String xml_remote) {
        Diff myDiff = DiffBuilder.compare(xml_original).withTest(xml_remote).ignoreComments().ignoreWhitespace().build();

        List<Difference> diffs = new ArrayList<>();
        myDiff.getDifferences().forEach(diffs::add);
        return diffs;
    }

    public String computeSHA(XmlDoc doc) throws Exception {
        Document d = doc.getParsedXml();
        stripEmptyElements(d);
        String s = XmlDoc.writeXML(d);
        String sha2 = blobStorageService.computeSHA2(s);
        return sha2;
    }


//    public XmlServiceRecordDoc parseXmlServiceDoc(HttpResult data) throws Exception {
//        XmlDoc xmlDoc = xmlDocumentFactory.create(new String(data.getData()));
//        if (!(xmlDoc instanceof XmlServiceRecordDoc ))
//            return null;
//        return (XmlServiceRecordDoc) xmlDoc;
//    }

    public boolean isXML(HttpResult result) {
        try {
            return capabilitiesContinueReadingPredicate.isXML(new String(result.getData()));
        } catch (Exception e) {
            return false;
        }
    }
}
