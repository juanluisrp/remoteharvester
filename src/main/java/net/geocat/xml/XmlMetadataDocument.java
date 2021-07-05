package net.geocat.xml;

import net.geocat.xml.helpers.OnlineResource;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.List;

public class XmlMetadataDocument extends XmlDoc {

    //i.e. service/dataset etc...
    String metadataDocumentType;
    String fileIdentifier;
    List<OnlineResource>  transferOptions = new ArrayList<>();
    List<OnlineResource>  connectPoints = new ArrayList<>();


    public XmlMetadataDocument(XmlDoc doc) throws  Exception {
        super(doc);
        if (!parsedXml.getFirstChild().getLocalName().equals("MD_Metadata"))
            throw new Exception("XmlMetadataDocument -- root node should be MD_Metadata");
        setup_XmlMetadataDocument();

    }

    public void setup_XmlMetadataDocument() throws   Exception {
        Node n = xpath_node("/gmd:MD_Metadata/gmd:hierarchyLevel/gmd:MD_ScopeCode/@codeListValue");
        metadataDocumentType = n.getTextContent();

        n = xpath_node("/gmd:MD_Metadata/gmd:fileIdentifier/gco:CharacterString");
        fileIdentifier = n.getTextContent();


        NodeList nl = xpath_nodeset("//gmd:transferOptions/gmd:MD_DigitalTransferOptions/gmd:onLine/gmd:CI_OnlineResource");
        transferOptions = OnlineResource.create(nl);

        nl = xpath_nodeset("//srv:containsOperations/srv:SV_OperationMetadata");
        connectPoints = OnlineResource.create(nl);
    }

}
