package com.geocat.ingester.model.harvester;

public enum EndpointJobState {
    CREATING,
    DETERMINING_WORK, WORK_DETERMINED, GETTING_RECORDS, RECORDS_RECEIVED
}
