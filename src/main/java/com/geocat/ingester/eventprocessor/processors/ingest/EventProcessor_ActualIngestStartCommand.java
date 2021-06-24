package com.geocat.ingester.eventprocessor.processors.ingest;

import com.geocat.ingester.eventprocessor.BaseEventProcessor;
import com.geocat.ingester.events.ingest.ActualIngestStartCommand;
import com.geocat.ingester.service.IngesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class EventProcessor_ActualIngestStartCommand extends BaseEventProcessor<ActualIngestStartCommand> {

    @Autowired
    IngesterService ingesterService;


    public EventProcessor_ActualIngestStartCommand() {
        super();
    }

    @Override
    public EventProcessor_ActualIngestStartCommand internalProcessing() throws Exception {
        ActualIngestStartCommand cmd = getInitiatingEvent();

        ingesterService.run(cmd.getJobId(), cmd.getHarvesterId());

        return this;
    }

    @Override
    public EventProcessor_ActualIngestStartCommand externalProcessing() {
        return this;
    }
}
