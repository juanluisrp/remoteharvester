package geocat.events.determinework;

import geocat.events.Event;

public class CSWEndpointWorkDetermined extends Event {


    private long endPointId;
    private String harvesterId;

    public CSWEndpointWorkDetermined() {
    }

    public CSWEndpointWorkDetermined(String harvesterId, long endPointId) {
        this.endPointId = endPointId;
        this.harvesterId = harvesterId;
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
}
