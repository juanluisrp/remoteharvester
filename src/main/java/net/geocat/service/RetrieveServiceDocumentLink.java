package net.geocat.service;

import net.geocat.database.linkchecker.entities.CapabilitiesDocument;
import net.geocat.database.linkchecker.entities.ServiceDocumentLink;
import net.geocat.database.linkchecker.service.CapabilitiesDocumentService;
import net.geocat.http.HttpResult;
import net.geocat.http.IHTTPRetriever;
import net.geocat.service.capabilities.CapabilitiesLinkFixer;
import net.geocat.service.downloadhelpers.RetrievableSimpleLinkDownloader;
import net.geocat.xml.XmlDoc;
import net.geocat.service.downloadhelpers.CapabilitiesContinueReadingPredicate;
import net.geocat.xml.helpers.CapabilitiesType;
import net.geocat.xml.helpers.CapabilityDeterminer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class RetrieveServiceDocumentLink {

    @Autowired
    @Qualifier("cookieAttachingRetriever")
    IHTTPRetriever retriever;

    @Autowired
    CapabilitiesContinueReadingPredicate capabilitiesContinueReadingPredicate;

    @Autowired
    CapabilityDeterminer capabilityDeterminer;

    @Autowired
    BlobStorageService blobStorageService;

    @Autowired
    CapabilitiesLinkFixer capabilitiesLinkFixer;

    @Autowired
    CapabilitiesDocumentService capabilitiesDocumentService;

    @Autowired
    RetrievableSimpleLinkDownloader retrievableSimpleLinkDownloader;

    public ServiceDocumentLink process(ServiceDocumentLink link) throws  Exception {


        link = (ServiceDocumentLink) retrievableSimpleLinkDownloader.process(link);

        if (!link.getUrlFullyRead())
            return link;


        CapabilitiesDocument capDoc =capabilitiesDocumentService.create(link);
        link.setCapabilitiesDocument(capDoc);
        link.setSha2(capDoc.getSha2());

        return link;
    }


    public CapabilitiesType determineCapabilityType(HttpResult result){
        try{
            String doc = new String(result.getData());
            XmlDoc xmlDoc = new XmlDoc(doc);
            return capabilityDeterminer.determineCapabilitiesType(xmlDoc);
        }
        catch (Exception e){
            return null;
        }
    }

    public boolean isXML(HttpResult result){
        try {
            return capabilitiesContinueReadingPredicate.isXML(new String(result.getData()));
        }
        catch (Exception e){
            return false;
        }
    }
}
