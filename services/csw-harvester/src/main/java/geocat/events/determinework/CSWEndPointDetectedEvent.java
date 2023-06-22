package geocat.events.determinework;

import geocat.events.Event;

public class CSWEndPointDetectedEvent extends Event {
    private boolean lookForNestedDiscoveryService;
    private String url;
    private long endPointId;
    private String harvesterId;
    private String filter;

    public CSWEndPointDetectedEvent() {
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

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    @Override
    public String toString() {
        return "CSWEndPointDetectedEvent for processID=" + harvesterId + ", endpoint=" + endPointId + ", url=" + url
                + ", with filter=" + filter + ",lookForNestedDiscoveryService=" + lookForNestedDiscoveryService;
    }
}
