package net.geocat.database.harvester.repos;


 import net.geocat.database.harvester.entities.MetadataRecord;
 import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

 import java.util.List;

@Component
@Scope("prototype")
public interface MetadataRecordRepo extends CrudRepository<MetadataRecord, Long> {

    List<MetadataRecord> findBySha2(String sha2);

    List<MetadataRecord> findByEndpointJobIdIn(List<Long> endpointjobIds);


    List<MetadataRecord> findByEndpointJobId(long endpointjobId);

    MetadataRecord findByEndpointJobIdAndRecordNumber(long endpointjobId, int recordNumber);

    long countByEndpointJobId(long endpointJobId);

    @Query(value = "Select count(distinct record_identifier) from metadata_record   where endpoint_job_id = ?1",
            nativeQuery = true
    )
    long countDistinctRecordIdentifierByEndpointJobId(long endpointJobId);
}
