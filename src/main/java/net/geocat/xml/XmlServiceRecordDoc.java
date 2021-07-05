package net.geocat.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathExpressionException;

public class XmlServiceRecordDoc extends XmlMetadataDocument {
    //i.e. 'view', 'download', 'discovery' ...

    String serviceType;
    String serviceTypeVersion;



    public XmlServiceRecordDoc(XmlDoc doc) throws Exception {
        super(doc);
        setup_XmlServiceRecordDoc();
    }

    public String getServiceType() {
        return serviceType;
    }





    public void setup_XmlServiceRecordDoc() throws  Exception {
        populateServiceType();
    }

    public void populateServiceType()  throws  Exception{
        Node n = xpath_node("//srv:serviceType/gco:LocalName");
        serviceType = n.getTextContent();
        n = xpath_node("//srv:serviceTypeVersion/gco:CharacterString");
        if (n != null)
         serviceTypeVersion = n.getTextContent();

    }

}
