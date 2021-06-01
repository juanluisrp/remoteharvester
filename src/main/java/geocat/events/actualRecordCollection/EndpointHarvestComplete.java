package geocat.events.actualRecordCollection;

import geocat.events.Event;

public class EndpointHarvestComplete extends Event {
    private String endPointId;
    private String harvestId;

    public EndpointHarvestComplete() {
    }

    public EndpointHarvestComplete(String endPointId, String harvestId) {
        this.endPointId = endPointId;
        this.harvestId = harvestId;
    }

    public String getEndPointId() {
        return endPointId;
    }

    public void setEndPointId(String endPointId) {
        this.endPointId = endPointId;
    }

    public String getHarvestId() {
        return harvestId;
    }

    public void setHarvestId(String harvestId) {
        this.harvestId = harvestId;
    }
}
