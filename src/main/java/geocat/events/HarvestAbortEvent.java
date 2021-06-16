package geocat.events;

public class HarvestAbortEvent extends Event {

    public String processID;

    public HarvestAbortEvent() {
    }

    public HarvestAbortEvent(String processID) {
        this.processID = processID;
    }


    public String getProcessID() {
        return processID;
    }

    public void setProcessID(String processID) {
        this.processID = processID;
    }

    @Override
    public String toString() {
        return "HarvestAbortEvent for processID="+processID;
    }

}
