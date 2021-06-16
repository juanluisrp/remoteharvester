package geocat.database.repos;


import geocat.database.entities.MetadataRecord;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public interface MetadataRecordRepo extends CrudRepository<MetadataRecord, Long> {


    MetadataRecord findByEndpointJobIdAndRecordNumber(long endpointjobId, int recordNumber);

    long countByEndpointJobId(long endpointJobId);

    @Query(value = "Select count(distinct record_identifier) from metadata_record   where endpoint_job_id = ?1",
            nativeQuery = true
    )
    long countDistinctRecordIdentifierByEndpointJobId(long endpointJobId);
}
