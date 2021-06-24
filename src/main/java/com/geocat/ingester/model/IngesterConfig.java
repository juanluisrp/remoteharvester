package com.geocat.ingester.model;

public class IngesterConfig {
    private String longTermTag;
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

    @Override
    public String toString() {
        return "{processID=" + processID + ", longTermTag=" + longTermTag + "}";
    }

}
