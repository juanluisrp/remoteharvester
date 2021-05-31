package geocat.csw;


import java.util.List;

public class CSWMetadata {
    private String harvesterId;
    private String endpointId;
    private int numberOfExpectedRecords;


    private boolean lookForNestedDiscoveryService;


    public boolean isLookForNestedDiscoveryService() {
        return lookForNestedDiscoveryService;
    }

    public void setLookForNestedDiscoveryService(boolean lookForNestedDiscoveryService) {
        this.lookForNestedDiscoveryService = lookForNestedDiscoveryService;
    }


    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    private String filter;
    public String getRecordsUrl;
    public List<List<String>> nestedGetCapUrls;

    public CSWMetadata(String harvesterId,
                       String endpointId,
                       int numberOfExpectedRecords,
                       String getRecordsUrl,
                       List<List<String>> nestedGetCapUrls,
                       String filter,
                       boolean lookForNestedDiscoveryService) {
        this.harvesterId = harvesterId;
        this.endpointId = endpointId;
        this.numberOfExpectedRecords = numberOfExpectedRecords;
        this.getRecordsUrl = getRecordsUrl;
        this.nestedGetCapUrls = nestedGetCapUrls;
        this.filter = filter;
        this.lookForNestedDiscoveryService=lookForNestedDiscoveryService;
    }

    public int getNumberOfExpectedRecords() {
        return numberOfExpectedRecords;
    }

    public void setNumberOfExpectedRecords(int numberOfExpectedRecords) {
        this.numberOfExpectedRecords = numberOfExpectedRecords;
    }

    public String getGetRecordsUrl() {
        return getRecordsUrl;
    }

    public void setGetRecordsUrl(String getRecordsUrl) {
        this.getRecordsUrl = getRecordsUrl;
    }

    public List<List<String>> getNestedGetCapUrls() {
        return nestedGetCapUrls;
    }

    public void setNestedGetCapUrls(List<List<String>> nestedGetCapUrls) {
        this.nestedGetCapUrls = nestedGetCapUrls;
    }

    public String getHarvesterId() {
        return harvesterId;
    }

    public void setHarvesterId(String harvesterId) {
        this.harvesterId = harvesterId;
    }

    public String getEndpointId() {
        return endpointId;
    }

    public void setEndpointId(String endpointId) {
        this.endpointId = endpointId;
    }
}
