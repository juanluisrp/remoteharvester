package net.geocat.xml;

import net.geocat.xml.helpers.DownloadServiceType;
import net.geocat.xml.helpers.DownloadServiceTypeProbe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import javax.xml.xpath.XPathExpressionException;

@Component
public class XmlDocumentFactory {

    @Autowired
    DownloadServiceTypeProbe downloadServiceTypeProbe;

    public XmlDoc create(String xml) throws Exception {
        XmlDoc doc = new XmlDoc(xml);
        if (isCSWServiceMetadataDocument(doc))
        {
            XmlServiceRecordDoc xmlServiceRecordDoc =  new XmlServiceRecordDoc(doc);

                return xmlServiceRecordDoc;

        }
        return doc;
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
