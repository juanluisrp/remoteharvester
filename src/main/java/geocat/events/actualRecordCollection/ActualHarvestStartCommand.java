package geocat.events.actualRecordCollection;

import geocat.events.Event;

public class ActualHarvestStartCommand extends Event {

    private String harvesterId;

    public ActualHarvestStartCommand() {
    }

    public ActualHarvestStartCommand(String harvesterId) {
        this.harvesterId = harvesterId;
    }

    public String getHarvesterId() {
        return harvesterId;
    }

    public void setHarvesterId(String harvesterId) {
        this.harvesterId = harvesterId;
    }

    @Override
    public String toString() {
        return "ActualHarvestStartCommand for processID="+harvesterId;
    }
}
