package net.geocat.database.linkchecker.repos;

import net.geocat.database.linkchecker.entities.LocalServiceMetadataRecord;
import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public interface LocalServiceMetadataRecordRepo extends CrudRepository<LocalServiceMetadataRecord, Long> {

}