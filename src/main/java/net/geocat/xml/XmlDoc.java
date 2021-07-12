package net.geocat.xml;

import org.springframework.util.xml.SimpleNamespaceContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

public class XmlDoc {

    String originalXmlString;
    Document parsedXml;

    String rootTagName;



    String rootNS;


    public XmlDoc (String xml) throws  Exception {
        this(xml,parseXML(xml));
    }

    public XmlDoc(String xml, Document xmlDoc) throws Exception {
        originalXmlString = xml;
        parsedXml = xmlDoc;
        setup_XmlDoc();
    }

    public XmlDoc(XmlDoc doc) throws Exception {
        originalXmlString = doc.originalXmlString;
        parsedXml = doc.parsedXml;
        setup_XmlDoc();
    }

    public void setup_XmlDoc() throws   Exception {
        rootTagName = parsedXml.getFirstChild().getLocalName();
        rootNS = parsedXml.getFirstChild().getNamespaceURI();
     }

    //again, to be consistent
    public static Document parseXML(String xml) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(false);
       // factory.setIgnoringElementContentWhitespace(true);
        factory.setFeature("http://xml.org/sax/features/namespaces", true);
        factory.setFeature("http://xml.org/sax/features/validation", false);
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);


        DocumentBuilder builder = factory.newDocumentBuilder();

        ByteArrayInputStream input = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        Document doc = builder.parse(input);
        return doc;
    }

    //this is here so we can be consistent with the XML writing (otherwise the SHA2 will change)
    public static String writeXML(Node doc) throws Exception {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;

        transformer = tf.newTransformer();

        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
       // transformer.setOutputProperty(OutputKeys.INDENT, "no");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
       transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        StringWriter writer = new StringWriter();

        transformer.transform(new DOMSource(doc), new StreamResult(writer));

        return writer.getBuffer().toString();
    }

    public static XPath createXPath(){
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();

        SimpleNamespaceContext nsCtx = new SimpleNamespaceContext();
        xpath.setNamespaceContext(nsCtx);
        nsCtx.bindNamespaceUri("gmd", "http://www.isotc211.org/2005/gmd");
        nsCtx.bindNamespaceUri("csw", "http://www.opengis.net/cat/csw/2.0.2");
        nsCtx.bindNamespaceUri("gco", "http://www.isotc211.org/2005/gco");

        nsCtx.bindNamespaceUri("gmx", "http://www.isotc211.org/2005/gmx");
        nsCtx.bindNamespaceUri("srv", "http://www.isotc211.org/2005/srv");

        nsCtx.bindNamespaceUri("inspire_common", "http://inspire.ec.europa.eu/schemas/common/1.0");
        nsCtx.bindNamespaceUri("inspire_vs", "http://inspire.ec.europa.eu/schemas/inspire_vs/1.0");
        nsCtx.bindNamespaceUri("inspire_dls", "http://inspire.ec.europa.eu/schemas/inspire_dls/1.0");

        nsCtx.bindNamespaceUri("atom", "http://www.w3.org/2005/Atom");


        return xpath;
    }

    public   org.w3c.dom.NodeList xpath_nodeset( String xpathStr) throws XPathExpressionException {
         XPath xpath = createXPath();


         return ((org.w3c.dom.NodeList) xpath.evaluate(xpathStr, this.parsedXml , XPathConstants.NODESET));
    }

    public static  org.w3c.dom.NodeList xpath_nodeset(Node doc, String xpathStr) throws XPathExpressionException {
        XPath xpath = createXPath();

        return ((org.w3c.dom.NodeList) xpath.evaluate(xpathStr, doc, XPathConstants.NODESET));
    }


    public   String xpath_attribute( String xpathStr, String attname) throws XPathExpressionException {
        XPath xpath = createXPath();

        String value = ((org.w3c.dom.Node) xpath.evaluate(xpathStr, this.parsedXml, XPathConstants.NODE)).getAttributes().getNamedItem(attname).getNodeValue();
        return value;
    }

    public static   String xpath_attribute( Node doc, String xpathStr, String attname) throws XPathExpressionException {
        XPath xpath = createXPath();

        Node n  =(org.w3c.dom.Node) xpath.evaluate(xpathStr, doc, XPathConstants.NODE);
        Node att = n.getAttributes().getNamedItem(attname);
        if (att == null)
            return null;
        String value = att.getNodeValue();
        return value;
    }

    public  Node xpath_node( String xpathStr) throws XPathExpressionException {
        XPath xpath = createXPath();

        return ((org.w3c.dom.Node) xpath.evaluate(xpathStr, this.parsedXml, XPathConstants.NODE));
    }

    public static Node xpath_node(Node doc, String xpathStr) throws XPathExpressionException {
        XPath xpath = createXPath();

        return ((org.w3c.dom.Node) xpath.evaluate(xpathStr, doc, XPathConstants.NODE));
    }

    //abc:def --> def
    public static String getLocalName(String fullName){
        int idx = fullName.indexOf(':');
        if (idx ==-1)
            return fullName;
        return fullName.substring(idx+1);
    }

    public String getRootTagName() {
        return rootTagName;
    }

    public String getRootNS() {
        return rootNS;
    }

    public String getOriginalXmlString() {
        return originalXmlString;
    }

    public Document getParsedXml() {
        return parsedXml;
    }
}
