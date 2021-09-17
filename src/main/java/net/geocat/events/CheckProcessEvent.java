package net.geocat.events;

public class CheckProcessEvent extends Event {

    public String orchestratorProcessId;

    public CheckProcessEvent(String orchestratorProcessId) {
        this.orchestratorProcessId = orchestratorProcessId;
    }

    public CheckProcessEvent() {
    }

    //----
    public String getOrchestratorProcessId() {
        return orchestratorProcessId;
    }

    public void setOrchestratorProcessId(String orchestratorProcessId) {
        this.orchestratorProcessId = orchestratorProcessId;
    }

    //---


    @Override
    public String toString() {
        return "CheckProcessEvent{" +
                "orchestratorProcessId='" + orchestratorProcessId + '\'' +
                '}';
    }
}
