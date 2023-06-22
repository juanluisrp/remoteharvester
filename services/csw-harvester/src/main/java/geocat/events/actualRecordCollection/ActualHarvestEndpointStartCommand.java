package geocat.events.actualRecordCollection;

import geocat.events.Event;

public class ActualHarvestEndpointStartCommand extends Event {
    private long endPointId;
    private String harvesterId;
    private String getRecordsURL;
    private String filter;
    private int nRecordPerRequest;

    private int expectedNumberOfRecords;
    private String recordQueueHint;
    private String actualGetRecordQueue;
    private Boolean doNotSort;


    public ActualHarvestEndpointStartCommand() {
    }

    public Boolean getDoNotSort() {
        return doNotSort;
    }

    public void setDoNotSort(Boolean doNotSort) {
        this.doNotSort = doNotSort;
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

    public int getnRecordPerRequest() {
        return nRecordPerRequest;
    }

    public void setnRecordPerRequest(int nRecordPerRequest) {
        this.nRecordPerRequest = nRecordPerRequest;
    }


    public int getExpectedNumberOfRecords() {
        return expectedNumberOfRecords;
    }

    public void setExpectedNumberOfRecords(int expectedNumberOfRecords) {
        this.expectedNumberOfRecords = expectedNumberOfRecords;
    }


    public String getRecordQueueHint() {
        return recordQueueHint;
    }

    public void setRecordQueueHint(String recordQueueHint) {
        this.recordQueueHint = recordQueueHint;
    }

    public String getActualGetRecordQueue() {
        return actualGetRecordQueue;
    }

    public void setActualGetRecordQueue(String actualGetRecordQueue) {
        this.actualGetRecordQueue = actualGetRecordQueue;
    }

    @Override
    public String toString() {
        return "ActualHarvestEndpointStartCommand for processID=" + harvesterId + ",endpoint=" + endPointId +
                ", getRecordsURL=" + getRecordsURL + " ,nRecordPerRequest=" + nRecordPerRequest
                + ",expectedNumberOfRecords=" + expectedNumberOfRecords + ",recordQueueHint=" + recordQueueHint
                + ",actualGetRecordQueue=" + actualGetRecordQueue
                + ", filter=" + filter;
    }
}
