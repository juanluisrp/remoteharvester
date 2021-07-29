package net.geocat.xml;

import org.w3c.dom.Node;

import javax.xml.xpath.XPathExpressionException;

public class XmlDatasetMetadataDocument extends XmlMetadataDocument {

    public String datasetIdentifier;

    public XmlDatasetMetadataDocument(XmlDoc doc) throws Exception {
        super(doc);
        setup_XmlDatasetMetadataDocument();
    }

    private void setup_XmlDatasetMetadataDocument() throws XPathExpressionException {
         Node n = xpath_node("/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/gmd:identifier/gmd:MD_Identifier/gmd:code/gco:CharacterString");
         if (n!=null)
             datasetIdentifier = n.getTextContent();
    }

    public String getDatasetIdentifier() {
        return datasetIdentifier;
    }

    public void setDatasetIdentifier(String datasetIdentifier) {
        this.datasetIdentifier = datasetIdentifier;
    }
}
