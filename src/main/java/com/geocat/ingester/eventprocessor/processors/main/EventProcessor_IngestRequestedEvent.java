package com.geocat.ingester.eventprocessor.processors.main;

import com.geocat.ingester.eventprocessor.BaseEventProcessor;
import com.geocat.ingester.events.Event;
import com.geocat.ingester.events.IngestEventFactory;
import com.geocat.ingester.events.IngestRequestedEvent;
import com.geocat.ingester.model.ingester.IngestJob;
import com.geocat.ingester.model.ingester.IngestJobState;
import com.geocat.ingester.service.IngestJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class EventProcessor_IngestRequestedEvent extends BaseEventProcessor<IngestRequestedEvent> {

    @Autowired
    IngestJobService ingestJobService;

    @Autowired
    IngestEventFactory eventFactory;


    IngestJob job;

    @Override
    public EventProcessor_IngestRequestedEvent internalProcessing() {
        ingestJobService.createNewIngestJobInDB(getInitiatingEvent());
        job = ingestJobService.updateIngestJobStateInDB(getInitiatingEvent().getJobId(), IngestJobState.CREATING);
        return this;
    }

    @Override
    public EventProcessor_IngestRequestedEvent externalProcessing() {
        return this;
    }

    @Override
    public List<Event> newEventProcessing() {
        List<Event> result = new ArrayList<>();
        Event newEvent = eventFactory.create_StartWorkCommand(job);
        result.add(newEvent);
        return result;
    }

}
