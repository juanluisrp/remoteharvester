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


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class MetadataService {

//
//
//    @Autowired
//    BlobStorageService blobStorageService;
//
//    //again, to be consistent
//    public static Document parseXML(String xml) throws ParserConfigurationException, IOException, SAXException {
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        factory.setNamespaceAware(true);
//        factory.setValidating(false);
//        factory.setFeature("http://xml.org/sax/features/namespaces", false);
//        factory.setFeature("http://xml.org/sax/features/validation", false);
//        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
//        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
//
//
//        DocumentBuilder builder = factory.newDocumentBuilder();
//
//        ByteArrayInputStream input = new ByteArrayInputStream(xml.getBytes("UTF-8"));
//        Document doc = builder.parse(input);
//        return doc;
//    }
//
//   p
//
//    //record individual records in the database, given a bunch of results.
//    public void explode(RecordSet recordSet, Document xml_getRecords) throws Exception {
//        NodeList metadataRecords = XMLTools.xpath_nodeset(xml_getRecords, "/GetRecordsResponse/SearchResults/MD_Metadata");
//        Map<String, String> extraNamespaces = extractNamespace(xml_getRecords);
//        for (int t = 0; t < metadataRecords.getLength(); t++) {
//            Node metadataRecord = metadataRecords.item(t);
//            addNamespaces(metadataRecord, extraNamespaces);
//            String xmlStr = writeXML(metadataRecord);
//            String identifier = extractIdentifier(metadataRecord);
//            String sha2 = blobStorageService.computeSHA2(xmlStr);
//            blobStorageService.ensureBlobExists(xmlStr, sha2);  //ok to do multiple times
//            MetadataRecord record = metadataRecordService.create(recordSet, t, sha2, identifier);
//        }
//    }
//
//    private String extractIdentifier(Node xml) throws XPathExpressionException {
//        return XMLTools.xpath_node(xml, "fileIdentifier/CharacterString").getTextContent();
//    }
//
//    private void addNamespaces(Node metadataRecord, Map<String, String> extraNamespaces) {
//        for (Map.Entry<String, String> ns : extraNamespaces.entrySet()) {
//            ((Element) metadataRecord).setAttribute(ns.getKey(), ns.getValue());
//        }
//    }
//
//    public Map<String, String> extractNamespace(Document d) throws XPathExpressionException {
//        Map<String, String> result = new HashMap<>();
//        NamedNodeMap response = XMLTools.xpath_node(d, "/GetRecordsResponse").getAttributes();
//        NamedNodeMap searchResults = XMLTools.xpath_node(d, "/GetRecordsResponse/SearchResults").getAttributes();
//
//        extractNamespaces(result, response);
//        extractNamespaces(result, searchResults);
//
//        return result;
//    }
//
//    private void extractNamespaces(Map<String, String> namespaces, NamedNodeMap attributes) {
//        for (int t = 0; t < attributes.getLength(); t++) {
//            Node node = attributes.item(t);
//            String name = attributes.item(t).getNodeName();
//            if (name.startsWith("xmlns:")) {
//                namespaces.put(name, node.getNodeValue());
//            }
//        }
//    }
//
//    //this is here so we can be consistent with the XML writing (otherwise the SHA2 will change)
//    protected String writeXML(Node doc) throws Exception {
//        TransformerFactory tf = TransformerFactory.newInstance();
//        Transformer transformer;
//
//        transformer = tf.newTransformer();
//
//        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
//        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
//
//        StringWriter writer = new StringWriter();
//
//        transformer.transform(new DOMSource(doc), new StreamResult(writer));
//
//        return writer.getBuffer().toString();
//    }


}
