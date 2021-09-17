package net.geocat.database.orchestrator.entities;

public enum OrchestratedHarvestProcessState {
    CREATED,
    HAVESTING,
    LINKCHECKING,
    INGESTING,

    COMPLETE, USERABORT, ERROR
}
