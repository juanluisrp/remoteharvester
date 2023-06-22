package com.geocat.ingester.model;

public class IngesterConfig {
    private String longTermTag;
    private String harvestJobId;

    private String processID;

    public String getProcessID() {
        return processID;
    }

    public void setProcessID(String processID) {
        this.processID = processID;
    }

    public String getLongTermTag() {
        return longTermTag;
    }

    public void setLongTermTag(String longTermTag) {
        this.longTermTag = longTermTag;
    }

    public String getHarvestJobId() {
        return harvestJobId;
    }

    public void setHarvestJobId(String harvestJobId) {
        this.harvestJobId = harvestJobId;
    }

    @Override
    public String toString() {
        return "{processID=" + processID + ", longTermTag=" + longTermTag + "}";
    }

}
