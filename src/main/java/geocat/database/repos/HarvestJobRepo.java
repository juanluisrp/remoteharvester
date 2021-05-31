package geocat.database.repos;

import geocat.database.entities.HarvestJob;
import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public interface HarvestJobRepo extends CrudRepository<HarvestJob, String> {
}
