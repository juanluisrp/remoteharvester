package net.geocat.database.repos;

import net.geocat.database.entities.BlobStorage;
import org.springframework.data.repository.CrudRepository;

public interface BlobStorageRepo extends CrudRepository<BlobStorage, String> {
}
