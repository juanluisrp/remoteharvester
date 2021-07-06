package net.geocat.xml;

import net.geocat.xml.helpers.OperatesOn;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.List;

public class XmlServiceRecordDoc extends XmlMetadataDocument {
    //i.e. 'view', 'download', 'discovery' ...

    String serviceType;
    String serviceTypeVersion;
    List<OperatesOn> operatesOns = new ArrayList<>();



    public XmlServiceRecordDoc(XmlDoc doc) throws Exception {
        super(doc);
        setup_XmlServiceRecordDoc();
    }

    public String getServiceType() {
        return serviceType;
    }





    public void setup_XmlServiceRecordDoc() throws  Exception {
        populateServiceType();
        populateOperatesOn();
    }

    private void populateOperatesOn() throws Exception {
        NodeList nl = xpath_nodeset("//srv:operatesOn");
        operatesOns = OperatesOn.create(nl);
    }

    public void populateServiceType()  throws  Exception{
        Node n = xpath_node("//srv:serviceType/gco:LocalName");
        serviceType = n.getTextContent();
        n = xpath_node("//srv:serviceTypeVersion/gco:CharacterString");
        if (n != null)
         serviceTypeVersion = n.getTextContent();
    }

    public String getServiceTypeVersion() {
        return serviceTypeVersion;
    }

    public List<OperatesOn> getOperatesOns() {
        return operatesOns;
    }
}
