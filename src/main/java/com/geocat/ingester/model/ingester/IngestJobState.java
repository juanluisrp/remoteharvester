package com.geocat.ingester.model.ingester;

public enum IngestJobState {
    CREATING,
    INGESTING_RECORDS, INDEXING_RECORDS, RECORDS_PROCESSED,
    ERROR
}
