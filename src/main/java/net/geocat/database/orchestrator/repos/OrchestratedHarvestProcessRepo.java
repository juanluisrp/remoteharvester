package net.geocat.database.orchestrator.repos;

import net.geocat.database.orchestrator.entities.OrchestratedHarvestProcess;
import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import javax.persistence.*;



@Component
@Scope("prototype")
public interface OrchestratedHarvestProcessRepo extends CrudRepository<OrchestratedHarvestProcess, String> {
}