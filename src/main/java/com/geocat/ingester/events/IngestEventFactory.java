package com.geocat.ingester.events;

import com.geocat.ingester.events.ingest.ActualIngestStartCommand;
import com.geocat.ingester.model.ingester.IngestJob;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class IngestEventFactory {

    public ActualIngestStartCommand create_StartWorkCommand(IngestJob job) {
        return new ActualIngestStartCommand(
                job.getJobId(), job.getLongTermTag()
        );
    }

}
