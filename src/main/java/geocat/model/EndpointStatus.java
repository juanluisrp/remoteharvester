package geocat.model;

import geocat.database.entities.EndpointJob;

public class EndpointStatus {
    public String url;
    public String urlGetRecords;
    public String state;
    public String createTimeUTC;
    public String lastUpdateUTC;

    public int expectedNumberOfRecords; //might be 0 if not processed yet
    public int numberOfRecordsReceived;

    public EndpointStatus(EndpointJob job, int numberOfRecordsReceived) {
        this.numberOfRecordsReceived = numberOfRecordsReceived;
        this.expectedNumberOfRecords = job.getExpectedNumberOfRecords() == null ? -1 : job.getExpectedNumberOfRecords();
        this.url = job.getUrl();
        this.urlGetRecords = job.getUrlGetRecords();
        this.state = job.getState().toString();
        this.createTimeUTC = job.getCreateTimeUTC().toInstant().toString();
        this.lastUpdateUTC = job.getLastUpdateUTC().toInstant().toString();
    }
}
