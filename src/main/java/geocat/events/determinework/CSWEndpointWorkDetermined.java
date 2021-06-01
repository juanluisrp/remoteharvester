package geocat.events.determinework;

import geocat.events.Event;

public class CSWEndpointWorkDetermined extends Event {


    private String endPointId;
    private String harvesterId;

    public CSWEndpointWorkDetermined() {
    }

    public CSWEndpointWorkDetermined(String harvesterId, String endPointId) {
        this.endPointId = endPointId;
        this.harvesterId = harvesterId;
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
}
