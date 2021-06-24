package com.geocat.ingester.events.ingest;


import com.geocat.ingester.events.Event;

public class IngestWorkStartCommand extends Event {

    private String harvestId;

    public IngestWorkStartCommand(String harvestId) {
        this.harvestId = harvestId;
    }

    public IngestWorkStartCommand() {
    }

    public String getHarvestId() {
        return harvestId;
    }

    public void setHarvestId(String harvestId) {
        this.harvestId = harvestId;
    }
}
