package geocat.events.determinework;

import geocat.events.Event;

public class CSWEndpointWorkDetermined extends Event {


    public CSWEndpointWorkDetermined() {
    }

    public CSWEndpointWorkDetermined( String harvesterId,String endPointId) {
        this.endPointId = endPointId;
        this.harvesterId = harvesterId;
    }

    public String getEndPointId() {
        return endPointId;
    }

    public void setEndPointId(String endPointId) {
        this.endPointId = endPointId;
    }

    private String endPointId;

    public String getHarvesterId() {
        return harvesterId;
    }

    public void setHarvesterId(String harvesterId) {
        this.harvesterId = harvesterId;
    }

    private String harvesterId;
}
