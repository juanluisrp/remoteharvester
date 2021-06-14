package geocat.events.actualRecordCollection;

import geocat.events.Event;

public class EndpointHarvestComplete extends Event {
    private long endPointId;
    private String harvestId;

    public EndpointHarvestComplete() {
    }

    public EndpointHarvestComplete(long endPointId, String harvestId) {
        this.endPointId = endPointId;
        this.harvestId = harvestId;
    }

    public long getEndPointId() {
        return endPointId;
    }

    public void setEndPointId(long endPointId) {
        this.endPointId = endPointId;
    }

    public String getHarvestId() {
        return harvestId;
    }

    public void setHarvestId(String harvestId) {
        this.harvestId = harvestId;
    }
}
