package net.geocat.database.harvester.repos;

 import net.geocat.database.harvester.entities.HarvestJob;
 import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public interface HarvestJobRepo extends CrudRepository<HarvestJob, String> {
}
