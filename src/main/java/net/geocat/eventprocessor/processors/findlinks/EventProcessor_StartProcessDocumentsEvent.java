package net.geocat.eventprocessor.processors.findlinks;

import net.geocat.database.harvester.entities.EndpointJob;
import net.geocat.database.harvester.entities.MetadataRecord;
import net.geocat.database.harvester.repos.EndpointJobRepo;
import net.geocat.database.harvester.repos.MetadataRecordRepo;
import net.geocat.database.linkchecker.service.MetadataDocumentService;
import net.geocat.eventprocessor.BaseEventProcessor;
import net.geocat.events.Event;
import net.geocat.events.EventFactory;
import net.geocat.events.findlinks.ProcessMetadataDocumentEvent;
import net.geocat.events.findlinks.StartProcessDocumentsEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
public class EventProcessor_StartProcessDocumentsEvent extends BaseEventProcessor<StartProcessDocumentsEvent> {

    Logger logger = LoggerFactory.getLogger(EventProcessor_StartProcessDocumentsEvent.class);

    @Autowired
    EndpointJobRepo endpointJobRepo;

    @Autowired
    MetadataRecordRepo metadataRecordRepo;

    @Autowired
    MetadataDocumentService metadataDocumentService;

    @Autowired
    EventFactory eventFactory;

    List<MetadataRecord> metadataRecords;

    @Override
    public EventProcessor_StartProcessDocumentsEvent externalProcessing() {
        return this;
    }


    @Override
    public EventProcessor_StartProcessDocumentsEvent internalProcessing() {

        List<EndpointJob> endpointJobs = endpointJobRepo.findByHarvestJobId(getInitiatingEvent().getHarvestJobId());
        List<Long> endpointIds = endpointJobs.stream().map(x -> x.getEndpointJobId()).collect(Collectors.toList());

        metadataRecords = metadataRecordRepo.findByEndpointJobIdIn(endpointIds);

        String linkCheckJobId = getInitiatingEvent().getLinkCheckJobId();
        for(MetadataRecord record : metadataRecords){
            metadataDocumentService.create(linkCheckJobId,record.getSha2(), record.getMetadataRecordId());
        }

         return this;
    }


    @Override
    public List<Event> newEventProcessing() {
        List<Event> result = new ArrayList<>();
        String linkCheckJobId = getInitiatingEvent().getLinkCheckJobId();
        String harvestJobId = getInitiatingEvent().getHarvestJobId();

        for(MetadataRecord record : metadataRecords){
            ProcessMetadataDocumentEvent e =
                    eventFactory.createProcessMetadataDocumentEvent(linkCheckJobId,harvestJobId, record.getEndpointJobId(),record.getSha2());
            result.add(e);
         }
        return result;
    }
}
