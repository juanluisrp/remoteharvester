package com.geocat.ingester.service;

import com.geocat.ingester.model.ingester.IngestJob;
import com.geocat.ingester.model.ingester.IngestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GetStatusService {

    @Autowired
    IngestJobService ingestJobService;

    public IngestStatus getStatus(String processId) {
        IngestJob job = ingestJobService.getById(processId);

        IngestStatus result = new IngestStatus(job);

        return result;
    }

    /*private long computeNumberReceived(EndpointJob endpointJob) {
        return metadataRecordRepo.countByEndpointJobId(endpointJob.getEndpointJobId());
    }*/
}
