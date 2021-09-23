package com.geocat.ingester.model.harvester;

public enum HarvestJobState {
    CREATING,
    DETERMINING_WORK, WORK_DETERMINED, GETTING_RECORDS, RECORDS_RECEIVED,
    ERROR, USERABORT,

    COMPLETE
}
