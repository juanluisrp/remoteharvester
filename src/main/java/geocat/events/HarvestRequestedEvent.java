package geocat.events;

public class HarvestRequestedEvent extends Event {


    String url;
    String harvestId;
    String filter;
    String problematicResultsConfigurationJSON;
    int numberRecordsPerRequest;
    String getRecordQueueHint;
    private String longTermTag;
    private boolean lookForNestedDiscoveryService;
    private Boolean doNotSort;

    public HarvestRequestedEvent() {
    }

    public Boolean getDoNotSort() {
        return doNotSort;
    }

    public void setDoNotSort(Boolean doNotSort) {
        this.doNotSort = doNotSort;
    }

    public int getNumberRecordsPerRequest() {
        return numberRecordsPerRequest;
    }

    public void setNumberRecordsPerRequest(int numberRecordsPerRequest) {
        this.numberRecordsPerRequest = numberRecordsPerRequest;
    }

    public String getLongTermTag() {
        return longTermTag;
    }

    public void setLongTermTag(String longTermTag) {
        this.longTermTag = longTermTag;
    }

    public boolean isLookForNestedDiscoveryService() {
        return lookForNestedDiscoveryService;
    }

    public void setLookForNestedDiscoveryService(boolean lookForNestedDiscoveryService) {
        this.lookForNestedDiscoveryService = lookForNestedDiscoveryService;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHarvestId() {
        return harvestId;
    }

    public void setHarvestId(String harvestId) {
        this.harvestId = harvestId;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getProblematicResultsConfigurationJSON() {
        return problematicResultsConfigurationJSON;
    }

    public void setProblematicResultsConfigurationJSON(String problematicResultsConfigurationJSON) {
        this.problematicResultsConfigurationJSON = problematicResultsConfigurationJSON;
    }

    public String getGetRecordQueueHint() {
        return getRecordQueueHint;
    }

    public void setGetRecordQueueHint(String getRecordQueueHint) {
        this.getRecordQueueHint = getRecordQueueHint;
    }

    @Override
    public String toString() {
        return "HarvestRequestedEvent for processID=" + harvestId + ", tag=" + longTermTag + ", url=" + url
                + ", with filter=" + filter + ",numberRecordsPerRequest=" + numberRecordsPerRequest
                + ", getRecordQueueHint=" + getRecordQueueHint + ",lookForNestedDiscoveryService=" + lookForNestedDiscoveryService;
    }

}
