package geocat.events.determinework;

import geocat.events.Event;

public class WorkedDeterminedFinished extends Event {
    private String harvestId;


    public WorkedDeterminedFinished(String harvestId) {
        this.harvestId = harvestId;
    }

    public WorkedDeterminedFinished() {
    }

    public String getHarvestId() {
        return harvestId;
    }

    public void setHarvestId(String harvestId) {
        this.harvestId = harvestId;
    }

    @Override
    public String toString() {
        return "WorkedDeterminedFinished for processID=" + harvestId;
    }
}
