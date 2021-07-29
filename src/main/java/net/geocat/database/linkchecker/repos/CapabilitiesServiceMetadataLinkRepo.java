package net.geocat.database.linkchecker.repos;

import net.geocat.database.linkchecker.entities.CapabilitiesServiceMetadataLink;
import net.geocat.database.linkchecker.entities.LinkCheckBlobStorage;
import org.springframework.data.repository.CrudRepository;


public interface CapabilitiesServiceMetadataLinkRepo extends CrudRepository<CapabilitiesServiceMetadataLink, Long> {
}
