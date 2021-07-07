package net.geocat.database.harvester.repos;

import net.geocat.database.harvester.entities.BlobStorage;
import org.springframework.data.repository.CrudRepository;

public interface BlobStorageRepo extends CrudRepository<BlobStorage, String> {
}
