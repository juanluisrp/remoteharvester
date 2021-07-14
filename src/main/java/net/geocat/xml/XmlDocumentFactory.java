package net.geocat.xml;

import net.geocat.xml.helpers.CapabilitiesType;
import net.geocat.xml.helpers.CapabilityDeterminer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathExpressionException;

@Component
public class XmlDocumentFactory {

//    @Autowired
//    DownloadServiceTypeProbe downloadServiceTypeProbe;

    @Autowired
    CapabilityDeterminer capabilityDeterminer;

    public XmlDoc create(String xml) throws Exception {
        XmlDoc doc = new XmlDoc(xml);
        doc = simplify(doc);

        if (isCSWServiceMetadataDocument(doc))
        {
            XmlServiceRecordDoc xmlServiceRecordDoc =  new XmlServiceRecordDoc(doc);
            return xmlServiceRecordDoc;
        }
        if (isCSWMetadataDocument(doc)){
            doc = new XmlMetadataDocument(doc);
        }
        if (isCapabilitiesDoc(doc)) {
            CapabilitiesType type = capabilityDeterminer.determineCapabilitiesType(doc);
            return XmlCapabilitiesDocument.create(doc,type);
        }
        return doc;
    }

    private XmlDoc simplify(XmlDoc doc) throws  Exception {
        if (doc.getRootTagName() .equals("GetRecordByIdResponse")) {
            Node n = doc.xpath_node("//gmd:MD_Metadata");
            if (n == null) // likely an empty response...
                return doc;
            Document d = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().newDocument();
            Node nn = d.importNode(n,true);

            d.appendChild(nn);
            return new XmlDoc(doc.getOriginalXmlString(),d);
        }
        return doc;
    }

    private boolean isCapabilitiesDoc(XmlDoc doc) {
        try{
             CapabilitiesType type = capabilityDeterminer.determineCapabilitiesType(doc);
             return true;
        }
        catch (Exception e){
            return false;
        }

    }

    public boolean isCSWMetadataDocument(XmlDoc xmlDoc){
        return  xmlDoc.parsedXml.getFirstChild().getLocalName().equals("MD_Metadata");
    }

    public boolean isCSWServiceMetadataDocument(XmlDoc xmlDoc) throws XPathExpressionException {
        if (!isCSWMetadataDocument(xmlDoc))
            return false;
        Node n = xmlDoc.xpath_node("/gmd:MD_Metadata/gmd:hierarchyLevel/gmd:MD_ScopeCode/@codeListValue");
        if (n==null)
            return false;
        if (n.getNodeValue().equals("service"))
            return true;
        return false;
    }

    public void determineUnderlyingServiceType(XmlDoc doc){

    }
}
