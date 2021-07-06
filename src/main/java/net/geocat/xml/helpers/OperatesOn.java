package net.geocat.xml.helpers;

import net.geocat.xml.XmlDoc;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class OperatesOn {

    String uuidref;
    String rawUrl;

    public static List<OperatesOn> create(NodeList nl) throws Exception {
        List<OperatesOn> result = new ArrayList<>(nl.getLength());
        for(int idx=0;idx<nl.getLength();idx++){
            Node n = nl.item(idx);
            OperatesOn opOn = new OperatesOn(n);
            result.add(opOn);
        }
        return result;
    }

    public OperatesOn(Node node) throws Exception {
        if (!node.getLocalName().equals("operatesOn") )
            throw new Exception("OperatesOn -- root node should be operatesOn");

        uuidref = XmlDoc.xpath_attribute(node,".","uuidref");
        rawUrl = XmlDoc.xpath_attribute(node,".","xlink:href");
    }

    public String getUuidref() {
        return uuidref;
    }

    public String getRawUrl() {
        return rawUrl;
    }
}
