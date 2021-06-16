package geocat.events.actualRecordCollection;

import geocat.events.Event;

public class ActualHarvestCompleted extends Event {

    private String harvestId;

    public ActualHarvestCompleted() {
    }

    public ActualHarvestCompleted(String harvestId) {
        this.harvestId = harvestId;
    }

    public String getHarvestId() {
        return harvestId;
    }

    public void setHarvestId(String harvestId) {
        this.harvestId = harvestId;
    }

    @Override
    public String toString() {
        return "ActualHarvestCompleted for processID="+harvestId;
    }
}
