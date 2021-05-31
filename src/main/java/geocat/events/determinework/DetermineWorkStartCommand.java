package geocat.events.determinework;

import geocat.events.Event;

public class DetermineWorkStartCommand extends Event {

    public DetermineWorkStartCommand(String harvestId, String initialUrl, String filter, boolean lookForNestedDiscoveryService) {
        this.harvestId = harvestId;
        this.initialUrl = initialUrl;
        this.filter = filter;
        this.lookForNestedDiscoveryService = lookForNestedDiscoveryService;
    }
    public boolean isLookForNestedDiscoveryService() {
        return lookForNestedDiscoveryService;
    }

    public void setLookForNestedDiscoveryService(boolean lookForNestedDiscoveryService) {
        this.lookForNestedDiscoveryService = lookForNestedDiscoveryService;
    }

    private boolean lookForNestedDiscoveryService;

    public DetermineWorkStartCommand() {
    }

    public String getInitialUrl() {
        return initialUrl;
    }
    public void setInitialUrl(String initialUrl) {
        this.initialUrl = initialUrl;
    }

    public String getHarvestId() {
        return harvestId;
    }

    public void setHarvestId(String harvestId) {
        this.harvestId = harvestId;
    }

    private String harvestId;
    private String initialUrl;

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    private String filter;

}
