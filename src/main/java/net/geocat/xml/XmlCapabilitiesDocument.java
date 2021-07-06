package net.geocat.xml;

import net.geocat.xml.helpers.CapabilitiesType;
import org.w3c.dom.Node;

import javax.xml.xpath.XPathExpressionException;

public class XmlCapabilitiesDocument extends XmlDoc{

    boolean hasExtendedCapabilities;
    String metadataUrlRaw;
    CapabilitiesType capabilitiesType;

    public static XmlCapabilitiesDocument create(XmlDoc doc, CapabilitiesType type) throws Exception {
        switch (type){
            case WFS:
                return new XmlCapabilitiesWFS(doc);
            case WMS:
                return new XmlCapabilitiesWMS(doc);
            case WMTS:
                return new XmlCapabilitiesWMTS(doc);
            case Atom:
                return new XmlCapabilitiesAtom(doc);
            case CSW:
                return new XmlCapabilitiesCSW(doc);
        }
        throw new Exception("XmlCapabilitiesDocument - unknown type");
    }

    public XmlCapabilitiesDocument(XmlDoc doc, CapabilitiesType type) throws Exception {
        super(doc);
        this.capabilitiesType = type;
        setup_XmlCapabilitiesDocument();
    }

    private void setup_XmlCapabilitiesDocument() throws  Exception {
        setupExtendedCap();
    }

    private void setupExtendedCap( ) throws  Exception {
        //we use this notation because of issues with NS accross servers
        Node n = xpath_node("//*[local-name()='ExtendedCapabilities']");
        Node nnn = xpath_node("//*[local-name()='feed']/*[local-name()='link'][@rel=\"describedby\"]/@href");

        hasExtendedCapabilities = (n != null) ||  (nnn != null);
        if (!hasExtendedCapabilities)
            return;

        if (n != null) {
            setup_extendedcap(n);
            return;
        }

        if (nnn != null) {
            setup_inspire_atom(nnn);
            return;
        }
    }

    private void setup_extendedcap(Node n) throws Exception {
        if (n !=null)
            this.metadataUrlRaw = n.getTextContent();
    }

    private void setup_inspire_atom(Node n) throws Exception {
        if (n !=null)
            this.metadataUrlRaw = n.getTextContent();
    }
}
