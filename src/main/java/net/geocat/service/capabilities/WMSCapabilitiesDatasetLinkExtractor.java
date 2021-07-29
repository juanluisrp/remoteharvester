package net.geocat.service.capabilities;

import com.sun.org.apache.xpath.internal.NodeSet;
import net.geocat.xml.XmlCapabilitiesDocument;
import net.geocat.xml.XmlDoc;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
@Scope("prototype")
public class WMSCapabilitiesDatasetLinkExtractor implements  ICapabilitiesDatasetLinkExtractor {

    @Override
    public List<DatasetLink> findLinks(XmlCapabilitiesDocument doc) throws  Exception {
        List<DatasetLink> result = new ArrayList<>();
        NodeList layers = doc.xpath_nodeset("//wms:Layer");
        for(int idx=0;idx<layers.getLength();idx++) {
            Node layer = layers.item(idx);
            DatasetLink link = processLayer(doc,layer);
            if (link != null)
                result.add(link);
        }

        return unique(result);
    }

    public static List<DatasetLink> unique(List<DatasetLink> list){
        HashSet hs = new HashSet();
        hs.addAll(list);
        list.clear();
        list.addAll(hs);
        return list;
    }

    public DatasetLink processLayer(XmlCapabilitiesDocument doc, Node layer) throws Exception {
        String identifier = searchIdentifier(doc,layer);
        String metadataUrl = searchMetadataUrl(doc,layer);

        if ( (identifier != null) || (metadataUrl !=null))
            return new DatasetLink(identifier,metadataUrl);
        return null;
    }

    private String findMetadataURL(Node layer) throws  Exception {
        Node n = XmlDoc.xpath_node(layer, "wms:MetadataURL/wms:OnlineResource");
        if (n == null)
            return null;
        Node att = n.getAttributes().getNamedItem("xlink:href");
        if (att == null)
            return null;
        return att.getTextContent();
    }



    private String searchMetadataUrl(XmlCapabilitiesDocument doc, Node layer) throws Exception {
        String metadataURL = findMetadataURL(layer);
        if ( (metadataURL !=null) && (!metadataURL.isEmpty()) )
            return metadataURL;
        Node parentLayer = layer.getParentNode();
        if (parentLayer.getLocalName().equals("Layer"))
            return searchMetadataUrl(doc,parentLayer);
        return null;
    }

    private String findIdentifier(Node layer) throws  Exception {
        Node n = XmlDoc.xpath_node(layer, "wms:Identifier");
        if (n == null)
            return null;
        return n.getTextContent();
    }


    public String searchIdentifier(XmlCapabilitiesDocument doc, Node layer) throws Exception {
        String localIdentifier = findIdentifier(layer);
        if ( (localIdentifier !=null) && (!localIdentifier.isEmpty()) )
            return localIdentifier;
        Node parentLayer = layer.getParentNode();
        if (parentLayer.getLocalName().equals("Layer"))
            return searchIdentifier(doc,parentLayer);
        return null;
    }

}
