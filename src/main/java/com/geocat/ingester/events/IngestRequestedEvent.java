package com.geocat.ingester.events;

public class IngestRequestedEvent extends Event {
    String jobId;
    private String longTermTag;

    public IngestRequestedEvent() {
    }

    public String getLongTermTag() {
        return longTermTag;
    }

    public void setLongTermTag(String longTermTag) {
        this.longTermTag = longTermTag;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
}
