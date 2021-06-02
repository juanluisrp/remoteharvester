package geocat.database.repos;


import geocat.database.entities.MetadataRecord;
import geocat.database.entities.RecordSet;
import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public interface MetadataRecordRepo extends CrudRepository<MetadataRecord, String> {
}
