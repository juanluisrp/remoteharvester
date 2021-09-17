package net.geocat.database.orchestrator.entities;

import javax.persistence.*;


@Entity
public class OrchestratedHarvestProcess {


    @Id
    @Column(columnDefinition = "varchar(40)")
    private String jobId;

    //state of the overall process
    @Enumerated(EnumType.STRING)
    private OrchestratedHarvestProcessState state;

    //---

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public OrchestratedHarvestProcessState getState() {
        return state;
    }

    public void setState(OrchestratedHarvestProcessState state) {
        this.state = state;
    }

    //---

}
