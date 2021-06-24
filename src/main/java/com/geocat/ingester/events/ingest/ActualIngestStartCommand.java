package com.geocat.ingester.events.ingest;

import com.geocat.ingester.events.Event;

public class ActualIngestStartCommand extends Event {

    private String harvesterId;
    private String jobId;

    public ActualIngestStartCommand() {
    }

    public ActualIngestStartCommand(String jobId, String harvesterId) {
        this.jobId = jobId;
        this.harvesterId = harvesterId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getHarvesterId() {
        return harvesterId;
    }

    public void setHarvesterId(String harvesterId) {
        this.harvesterId = harvesterId;
    }

}
