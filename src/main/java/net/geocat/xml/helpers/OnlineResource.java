package net.geocat.xml.helpers;

import net.geocat.xml.XmlDoc;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//represents a <gmd:CI_OnlineResource>
// can also have the operationName if the CI_OnlineResource comes from a containsOperation
public class OnlineResource {
    Node CI_OnlineResource;
    String operationName;

    String rawURL;
    String protocol;
    String function;

    public static List<OnlineResource> create(NodeList nl) throws Exception {
        List<OnlineResource> result = new ArrayList<>(nl.getLength());
        for(int idx=0;idx<nl.getLength();idx++){
            Node n = nl.item(idx);
            List<OnlineResource> resources = create(n);
            result.addAll(resources);
        }
        return result;
    }

    public static  List<OnlineResource> create(Node n) throws Exception {
        if (n.getLocalName().equals("CI_OnlineResource"))
            return Arrays.asList(new OnlineResource(n));
        //
        List<OnlineResource> result = new ArrayList<>();
        if (n.getLocalName().equals("SV_OperationMetadata")){
            String opName = null;
            Node nn = XmlDoc.xpath_node(n,"srv:operationName/gco:CharacterString");
            if (nn !=null)
                opName = nn.getTextContent();
            NodeList nl = XmlDoc.xpath_nodeset(n,"srv:connectPoint/gmd:CI_OnlineResource");
            for(int idx=0;idx<nl.getLength();idx++){
                Node nnn = nl.item(idx);
                result.add(new OnlineResource(nnn,opName));
            }
            return result;
        }
        throw new Exception("dont know how to parse");
    }

    public OnlineResource(Node node ) throws Exception {
        this(node, null);
    }

    public OnlineResource(Node node, String operationName) throws Exception {
        if (!node.getLocalName().equals("CI_OnlineResource") )
            throw new Exception("OnlineResource -- root node should be CI_OnlineResource");

        this.CI_OnlineResource = node;
        this.operationName =operationName;

        parse();
    }

    private void parse() throws XPathExpressionException {
        //URL
        Node urlNode = XmlDoc.xpath_node(CI_OnlineResource,"gmd:linkage/gmd:URL");
        if (urlNode !=null)
            rawURL = urlNode.getTextContent();


        //Protocol
        Node protocolNode = XmlDoc.xpath_node(CI_OnlineResource,"gmd:protocol/gco:CharacterString");
        if ( (protocolNode !=null)) {
            protocol = protocolNode.getTextContent();
            if (protocol.equals("null"))
                protocol = null;
        }

        //function
        Node functionNode = XmlDoc.xpath_node(CI_OnlineResource,"gmd:function/gmd:CI_OnLineFunctionCode/@codeListValue");
        if (functionNode !=null)
            function = functionNode.getTextContent();
    }

}
