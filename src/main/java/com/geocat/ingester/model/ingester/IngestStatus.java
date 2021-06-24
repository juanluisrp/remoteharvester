package com.geocat.ingester.model.ingester;


public class IngestStatus {
    public String processID;
    public String longTermTag;
    public String state;
    public String createTimeUTC;
    public String lastUpdateUTC;
    public long totalRecords;
    public long numberOfRecordsIngested;
    public long numberOfRecordsIndexed;

    public IngestStatus(IngestJob job) {
        this.processID = job.getJobId();
        this.totalRecords = (job.getTotalRecords() == null?0:job.getTotalRecords());
        this.numberOfRecordsIngested = (job.getTotalIngestedRecords() == null?0:job.getTotalIngestedRecords());
        this.numberOfRecordsIndexed = (job.getTotalIndexedRecords() == null?0:job.getTotalIndexedRecords());
        this.longTermTag = job.getLongTermTag();
        this.state = job.getState().toString();
        this.createTimeUTC = job.getCreateTimeUTC().toInstant().toString();
        this.lastUpdateUTC = job.getLastUpdateUTC().toInstant().toString();
    }
}
