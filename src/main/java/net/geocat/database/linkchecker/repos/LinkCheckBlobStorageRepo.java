package net.geocat.database.linkchecker.repos;

 import net.geocat.database.linkchecker.entities.LinkCheckBlobStorage;
 import org.springframework.data.repository.CrudRepository;

public interface LinkCheckBlobStorageRepo extends CrudRepository<LinkCheckBlobStorage, String> {
}
