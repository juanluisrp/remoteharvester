package net.geocat.service;

import net.geocat.database.orchestrator.entities.OrchestratedHarvestProcess;
import net.geocat.database.orchestrator.entities.OrchestratedHarvestProcessState;
import net.geocat.database.orchestrator.repos.OrchestratedHarvestProcessRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Scope("prototype")
public class OrchestratedHarvestService {

    @Autowired
    OrchestratedHarvestProcessRepo orchestratedHarvestProcessRepo;

    public OrchestratedHarvestProcess createOrchestratedHarvestProcess(String processId) {
        Optional<OrchestratedHarvestProcess> job = orchestratedHarvestProcessRepo.findById(processId);
        if (job.isPresent()) {
            return job.get();
        }
        OrchestratedHarvestProcess result = new OrchestratedHarvestProcess();
        result.setJobId(processId);
        result.setState(OrchestratedHarvestProcessState.CREATED);
        return orchestratedHarvestProcessRepo.save(result);
    }


}
