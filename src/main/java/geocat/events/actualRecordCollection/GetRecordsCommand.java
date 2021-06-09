package geocat.events.actualRecordCollection;

import geocat.events.Event;

public class GetRecordsCommand extends Event {
    private String endPointId;
    private String harvesterId;
    private String getRecordsURL;
    private String filter;
    private int startRecordNumber;
    private int endRecordNumber;
    private String recordSetId;
    private boolean lastSet;
    private String workQueueName;
    private int totalRecordsInQuery;

    public GetRecordsCommand() {
    }


    public int getTotalRecordsInQuery() {
        return totalRecordsInQuery;
    }

    public void setTotalRecordsInQuery(int totalRecordsInQuery) {
        this.totalRecordsInQuery = totalRecordsInQuery;
    }



    public String getRecordSetId() {
        return recordSetId;
    }

    public void setRecordSetId(String recordSetId) {
        this.recordSetId = recordSetId;
    }

    public String getEndPointId() {
        return endPointId;
    }

    public void setEndPointId(String endPointId) {
        this.endPointId = endPointId;
    }

    public String getHarvesterId() {
        return harvesterId;
    }

    public void setHarvesterId(String harvesterId) {
        this.harvesterId = harvesterId;
    }

    public String getGetRecordsURL() {
        return getRecordsURL;
    }

    public void setGetRecordsURL(String getRecordsURL) {
        this.getRecordsURL = getRecordsURL;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public int getStartRecordNumber() {
        return startRecordNumber;
    }

    public void setStartRecordNumber(int startRecordNumber) {
        this.startRecordNumber = startRecordNumber;
    }

    public int getEndRecordNumber() {
        return endRecordNumber;
    }

    public void setEndRecordNumber(int endRecordNumber) {
        this.endRecordNumber = endRecordNumber;
    }

    public int expectedNumberOfRecords() {
        return (endRecordNumber - startRecordNumber) + 1;
    }

    @Override
    public String toString() {
        return "GetRecordsCommand - " + startRecordNumber + " to " + endRecordNumber + " (of "+totalRecordsInQuery+") from " + getRecordsURL;
    }

    public boolean isLastSet() {
        return lastSet;
    }

    public void setLastSet(boolean lastSet) {
        this.lastSet = lastSet;
    }

    public String getWorkQueueName() {
        return workQueueName;
    }

    public void setWorkQueueName(String workQueueName) {
        this.workQueueName = workQueueName;
    }
}
