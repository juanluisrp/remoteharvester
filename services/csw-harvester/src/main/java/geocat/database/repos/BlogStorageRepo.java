package geocat.database.repos;

import geocat.database.entities.BlobStorage;
import org.springframework.data.repository.CrudRepository;

public interface BlogStorageRepo extends CrudRepository<BlobStorage, String> {
}
