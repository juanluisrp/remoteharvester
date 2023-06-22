package geocat.events.actualRecordCollection;

import geocat.events.Event;

public class GetRecordsCommand extends Event {
    private long endPointId;
    private String harvesterId;
    private String getRecordsURL;
    private String filter;
    private int startRecordNumber;
    private int endRecordNumber;
    private long recordSetId;
    private boolean lastSet;
    private String workQueueName;
    private int totalRecordsInQuery;
    private Boolean doNotSort;

    public GetRecordsCommand() {
    }

    public Boolean getDoNotSort() {
        return doNotSort;
    }

    public void setDoNotSort(Boolean doNotSort) {
        this.doNotSort = doNotSort;
    }

    public int getTotalRecordsInQuery() {
        return totalRecordsInQuery;
    }

    public void setTotalRecordsInQuery(int totalRecordsInQuery) {
        this.totalRecordsInQuery = totalRecordsInQuery;
    }


    public long getRecordSetId() {
        return recordSetId;
    }

    public void setRecordSetId(long recordSetId) {
        this.recordSetId = recordSetId;
    }

    public long getEndPointId() {
        return endPointId;
    }

    public void setEndPointId(long endPointId) {
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

    @Override
    public String toString() {
        return "GetRecordsCommand - " + startRecordNumber + " to " + endRecordNumber + " (of " + totalRecordsInQuery + ") from " + getRecordsURL
                + ", harvesterId=" + harvesterId + ", endpointid=" + endPointId + ", filter=" + filter + ", recordSetId=" + recordSetId + ", islastSet=" + lastSet
                + ", workQueueName=" + workQueueName + ", totalRecordsInQuery=" + totalRecordsInQuery;
    }
}
