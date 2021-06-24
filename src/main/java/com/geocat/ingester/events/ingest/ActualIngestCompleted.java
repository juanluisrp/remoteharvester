package com.geocat.ingester.events.ingest;


import com.geocat.ingester.events.Event;

public class ActualIngestCompleted extends Event {

    private String harvestId;
    private String jobId;

    public ActualIngestCompleted() {
    }

    public ActualIngestCompleted(String harvestId) {
        this.harvestId = harvestId;
    }

    public String getHarvestId() {
        return harvestId;
    }

    public void setHarvestId(String harvestId) {
        this.harvestId = harvestId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
}
