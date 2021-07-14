package net.geocat.eventprocessor.processors.findlinks;


import net.geocat.database.harvester.entities.EndpointJob;
import net.geocat.database.harvester.entities.MetadataRecord;
import net.geocat.database.harvester.repos.BlobStorageRepo;
import net.geocat.database.linkchecker.entities.Link;
import net.geocat.database.linkchecker.entities.MetadataDocument;
import net.geocat.database.linkchecker.entities.MetadataDocumentState;
import net.geocat.database.linkchecker.repos.LinkRepo;
import net.geocat.database.linkchecker.repos.MetadataDocumentRepo;
import net.geocat.database.linkchecker.service.MetadataDocumentService;
import net.geocat.eventprocessor.BaseEventProcessor;
import net.geocat.events.Event;
import net.geocat.events.EventFactory;
import net.geocat.events.findlinks.LinksFoundInAllDocuments;
import net.geocat.events.findlinks.MetadataDocumentProcessedEvent;
import net.geocat.events.findlinks.ProcessMetadataDocumentEvent;
import net.geocat.events.findlinks.StartProcessDocumentsEvent;
import net.geocat.service.BlobStorageService;
import net.geocat.service.LinkFactory;
import net.geocat.service.ServiceDocLinkExtractor;
import net.geocat.xml.XmlDoc;
import net.geocat.xml.XmlDocumentFactory;
import net.geocat.xml.XmlMetadataDocument;
import net.geocat.xml.XmlServiceRecordDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
public class EventProcessor_ProcessMetadataDocumentEvent extends BaseEventProcessor<ProcessMetadataDocumentEvent> {

    Logger logger = LoggerFactory.getLogger(EventProcessor_ProcessMetadataDocumentEvent.class);

    @Autowired
    LinkFactory linkFactory;

    @Autowired
    BlobStorageService blobStorageService;

    @Autowired
    MetadataDocumentService metadataDocumentService;

    @Autowired
    MetadataDocumentRepo metadataDocumentRepo;

    @Autowired
    XmlDocumentFactory xmlDocumentFactory;

    @Autowired
    ServiceDocLinkExtractor serviceDocLinkExtractor;

    @Autowired
    LinkRepo linkRepo;

    @Autowired
    EventFactory eventFactory;


    String xml;
    XmlDoc doc;

    @Override
    public EventProcessor_ProcessMetadataDocumentEvent externalProcessing() throws Exception {
        String sha2 = getInitiatingEvent().getSha2();
        long endpointJobId = getInitiatingEvent().getEndpointJobId();
        xml = blobStorageService.findXML(sha2);
        doc = xmlDocumentFactory.create(xml);
        return this;
    }


    @Override
    public EventProcessor_ProcessMetadataDocumentEvent internalProcessing() throws Exception {
        String sha2 = getInitiatingEvent().getSha2();
        long endpointJobId = getInitiatingEvent().getEndpointJobId();
        String harvestJobId = getInitiatingEvent().getHarvestJobId();
        String linkCheckJob = getInitiatingEvent().getLinkCheckJobId();

        MetadataDocument metadataDocument = metadataDocumentService.find(linkCheckJob,sha2);
        if (!(doc instanceof XmlMetadataDocument)) {
            // this shouldn't happen
            metadataDocumentService.setState(metadataDocument , MetadataDocumentState.NOT_APPLICABLE);
            return this;
        }
        XmlMetadataDocument xmlMetadataDocument = (XmlMetadataDocument) doc;
        metadataDocument.setMetadataRecordType(xmlMetadataDocument.getMetadataDocumentType());
        metadataDocument.setRecordIdentifier(xmlMetadataDocument.getFileIdentifier());

        if (!(doc instanceof XmlServiceRecordDoc)) {
            // ignore - not a service record
            metadataDocumentService.setState(metadataDocument , MetadataDocumentState.NOT_APPLICABLE);
            return this;
        }

        XmlServiceRecordDoc xmlServiceRecordDoc = (XmlServiceRecordDoc) doc;

        String serviceType = xmlServiceRecordDoc.getServiceType();
        metadataDocument.setMetadataServiceType(serviceType);

        if (serviceType == null){
            metadataDocumentService.setState(metadataDocument , MetadataDocumentState.NOT_APPLICABLE);
            return this;
        }
        if (!serviceType.equalsIgnoreCase("view")
                && !serviceType.equalsIgnoreCase("download")
                && !serviceType.equalsIgnoreCase("discovery") ){
            metadataDocumentService.setState(metadataDocument, MetadataDocumentState.NOT_APPLICABLE);
            return this;
        }

        List<Link> links= serviceDocLinkExtractor.extractLinks(xmlServiceRecordDoc,sha2,harvestJobId,endpointJobId,linkCheckJob);

        linkRepo.saveAll(links);

        metadataDocument.setNumberOfLinksFound(links.size());
        metadataDocumentService.setState(metadataDocument , MetadataDocumentState.LINKS_EXTRACTED);
        return this;
    }


    @Override
    public List<Event> newEventProcessing() {
        List<Event> result = new ArrayList<>();

//        MetadataDocumentProcessedEvent e =eventFactory.createMetadataDocumentProcessedEvent(getInitiatingEvent());
//        result.add(e);

        if (metadataDocumentService.complete(getInitiatingEvent().getLinkCheckJobId()))
        {
            LinksFoundInAllDocuments e = eventFactory.createLinksFoundInAllDocuments(getInitiatingEvent());
            result.add(e);
        }

        return result;
    }
}
