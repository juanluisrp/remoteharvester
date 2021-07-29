package net.geocat.database.linkchecker.repos;

import net.geocat.database.linkchecker.entities.OperatesOnLink;
import net.geocat.database.linkchecker.entities.RemoteServiceMetadataRecordLink;
import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public interface RemoteServiceMetadataRecordLinkRepo extends CrudRepository<RemoteServiceMetadataRecordLink, Long> {

}