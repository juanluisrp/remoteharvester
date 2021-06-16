package geocat.events.determinework;

import geocat.events.Event;

public class DetermineWorkStartCommand extends Event {

    private boolean lookForNestedDiscoveryService;
    private String harvestId;
    private String initialUrl;
    private String filter;

    public DetermineWorkStartCommand(String harvestId, String initialUrl, String filter, boolean lookForNestedDiscoveryService) {
        this.harvestId = harvestId;
        this.initialUrl = initialUrl;
        this.filter = filter;
        this.lookForNestedDiscoveryService = lookForNestedDiscoveryService;
    }

    public DetermineWorkStartCommand() {
    }

    public boolean isLookForNestedDiscoveryService() {
        return lookForNestedDiscoveryService;
    }

    public void setLookForNestedDiscoveryService(boolean lookForNestedDiscoveryService) {
        this.lookForNestedDiscoveryService = lookForNestedDiscoveryService;
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

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    @Override
    public String toString() {
        return "DetermineWorkStartCommand for processID="+harvestId+", initialUrl="+initialUrl
                +", filter="+filter+", lookForNestedDiscoveryService="+lookForNestedDiscoveryService;
    }
}
