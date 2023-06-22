package geocat.csw.csw;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class XMLTools {


    public static Document parseXML(String xml) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        factory.setValidating(false);
        factory.setFeature("http://xml.org/sax/features/namespaces", false);
        factory.setFeature("http://xml.org/sax/features/validation", false);
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);


        DocumentBuilder builder = factory.newDocumentBuilder();

        ByteArrayInputStream input = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        Document doc = builder.parse(input);
        return doc;
    }


    public static org.w3c.dom.NodeList xpath_nodeset(Document doc, String xpathStr) throws XPathExpressionException {
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();

        return ((org.w3c.dom.NodeList) xpath.evaluate(xpathStr, doc, XPathConstants.NODESET));
    }

    public static org.w3c.dom.NodeList xpath_nodeset(Node doc, String xpathStr) throws XPathExpressionException {
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();

        return ((org.w3c.dom.NodeList) xpath.evaluate(xpathStr, doc, XPathConstants.NODESET));
    }


    public static String xpath_attribute(Document doc, String xpathStr, String attname) throws XPathExpressionException {
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();

        String value = ((org.w3c.dom.Node) xpath.evaluate(xpathStr, doc, XPathConstants.NODE)).getAttributes().getNamedItem(attname).getNodeValue();
        return value;
    }

    public static Node xpath_node(Document doc, String xpathStr) throws XPathExpressionException {
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();

        return ((org.w3c.dom.Node) xpath.evaluate(xpathStr, doc, XPathConstants.NODE));
    }

    public static Node xpath_node(Node doc, String xpathStr) throws XPathExpressionException {
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();

        return ((org.w3c.dom.Node) xpath.evaluate(xpathStr, doc, XPathConstants.NODE));
    }

}
