package net.geocat.service;


import net.geocat.database.linkchecker.entities.IndicatorStatus;
import net.geocat.database.linkchecker.entities.Link;
import net.geocat.http.HttpResult;
import net.geocat.http.IHTTPRetriever;
import net.geocat.xml.XmlDoc;
import net.geocat.xml.XmlDocumentFactory;
import net.geocat.xml.XmlMetadataDocument;
import net.geocat.xml.XmlServiceRecordDoc;
import net.geocat.xml.helpers.CapabilitiesContinueReadingPredicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.Difference;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@Component
public class LinkProcessor_GetCapLinkedMetadata implements ILinkProcessor {

    @Autowired
    @Qualifier("cookieAttachingRetriever")
    IHTTPRetriever retriever;

    @Autowired
    CapabilitiesContinueReadingPredicate capabilitiesContinueReadingPredicate;

    @Autowired
    XmlDocumentFactory xmlDocumentFactory;

    @Autowired
    BlobStorageService blobStorageService;

    @Override
    public Link process(Link link) throws Exception {
        if ( (link.getServiceMetadataLinkURL() == null ) || (link.getServiceMetadataLinkURL().isEmpty()) )
            return link; //nothing to do



        HttpResult data = null;
        try {
            data = retriever.retrieveXML("GET",link.getServiceMetadataLinkURL(),null,null,null);
        }
        catch(Exception e){
            link.setIndicator_ResolveServiceMetadataLink(IndicatorStatus.FAIL);
            link.setResolveServiceMetadataLinkException(e.getClass().getSimpleName()+" - "+e.getMessage());
            return link;
        }


        if ( (data.getHttpCode() != 200) ) {
            link.setIndicator_ResolveServiceMetadataLink(IndicatorStatus.FAIL);
            return link;
        }

        link.setIndicator_ResolveServiceMetadataLink(IndicatorStatus.PASS);

        if (!isXML(data)) {
            link.setIndicator_MetadataLinkIsXML(IndicatorStatus.FAIL);
            return link;
        }
        link.setIndicator_MetadataLinkIsXML(IndicatorStatus.PASS);

        XmlDoc xmlDoc = xmlDocumentFactory.create(new String(data.getData()));
        if (!(xmlDoc instanceof XmlMetadataDocument)) {
            link.setIndicator_MetadataLinkIsMD_METADATA(IndicatorStatus.FAIL);
            return link;
        }
        link.setIndicator_MetadataLinkIsMD_METADATA(IndicatorStatus.PASS);

        XmlMetadataDocument xmlServiceRecordDoc = (XmlMetadataDocument) xmlDoc;
        link.setMetadataLinkMetadataType(xmlServiceRecordDoc.getMetadataDocumentType());


        if (!(xmlDoc instanceof XmlServiceRecordDoc)) {
            link.setIndicator_MetadataLinkIsServiceRecord(IndicatorStatus.FAIL);
            return link;
        }
         link.setIndicator_MetadataLinkIsServiceRecord(IndicatorStatus.PASS);

        link.setMetadataLinkFileIdentifier( xmlServiceRecordDoc.getFileIdentifier());

        if (!xmlServiceRecordDoc.getFileIdentifier().equals(link.getOriginatingServiceRecordFileIdentifier())) {
            link.setIndicator_CompareServiceMetadataLink_FileIdentifier(IndicatorStatus.FAIL);
            return link;
        }
        link.setIndicator_CompareServiceMetadataLink_FileIdentifier(IndicatorStatus.PASS);


        String xml_original = blobStorageService.findXML(link.getOriginatingServiceRecordSHA2());
        String xml_remote = XmlDoc.writeXML(xmlServiceRecordDoc.getParsedXml());
        List<Difference> diffs = areSame(xml_original,xml_remote);


        if (!diffs.isEmpty())
        {
            link.setIndicator_CompareServiceMetadataLink_Full(IndicatorStatus.FAIL);
            String fullDiff = diffs.toString();
            link.setMetadataRecordDifferences(fullDiff.substring(0,Math.min(2000,fullDiff.length())));
            return link;
        }
        link.setIndicator_CompareServiceMetadataLink_Full(IndicatorStatus.PASS);


        return link;
    }

    private List<Difference> areSame(String xml_original, String xml_remote) {
        Diff myDiff = DiffBuilder.compare(xml_original).withTest(xml_remote).ignoreComments().ignoreWhitespace().build();

        List<Difference>  diffs = new ArrayList<>();
        myDiff.getDifferences() .forEach(diffs::add);
        return diffs;
    }

    public static void stripEmptyElements(Node node)
    {
        NodeList children = node.getChildNodes();
        for(int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            if(child.getNodeType() == Node.TEXT_NODE) {
                if (child.getTextContent().trim().length() == 0) {
                    child.getParentNode().removeChild(child);
                    i--;
                }
            }
            stripEmptyElements(child);
        }
    }

    public String computeSHA(XmlDoc doc) throws Exception {
        Document d = doc.getParsedXml();
        stripEmptyElements(d);
        String s = XmlDoc.writeXML(d);
        String sha2 = blobStorageService.computeSHA2(s);
        return sha2;
    }


//    public XmlServiceRecordDoc parseXmlServiceDoc(HttpResult data) throws Exception {
//        XmlDoc xmlDoc = xmlDocumentFactory.create(new String(data.getData()));
//        if (!(xmlDoc instanceof XmlServiceRecordDoc ))
//            return null;
//        return (XmlServiceRecordDoc) xmlDoc;
//    }

    public boolean isXML(HttpResult result){
        try {
            return capabilitiesContinueReadingPredicate.isXML(new String(result.getData()));
        }
        catch (Exception e){
            return false;
        }
    }
}
