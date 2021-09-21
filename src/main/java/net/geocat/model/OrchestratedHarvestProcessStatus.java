package net.geocat.model;

import net.geocat.database.orchestrator.entities.OrchestratedHarvestProcessState;

import java.util.ArrayList;
import java.util.List;

public class OrchestratedHarvestProcessStatus {
    String processID;
    OrchestratedHarvestProcessState orchestratedHarvestProcessState;

    public List<String> errorMessage;
    public List<List<String>> stackTraces;

    public OrchestratedHarvestProcessStatus(String processID, OrchestratedHarvestProcessState orchestratedHarvestProcessState) {
        this.processID = processID;
        this.orchestratedHarvestProcessState = orchestratedHarvestProcessState;

        errorMessage = new ArrayList<>();
        stackTraces = new ArrayList<>();
    }

    public String getProcessID() {
        return processID;
    }

    public void setProcessID(String processID) {
        this.processID = processID;
    }

    public OrchestratedHarvestProcessState getOrchestratedHarvestProcessState() {
        return orchestratedHarvestProcessState;
    }

    public void setOrchestratedHarvestProcessState(OrchestratedHarvestProcessState orchestratedHarvestProcessState) {
        this.orchestratedHarvestProcessState = orchestratedHarvestProcessState;
    }
}
