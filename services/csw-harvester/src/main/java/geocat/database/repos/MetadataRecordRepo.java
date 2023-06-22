package geocat.database.repos;


import geocat.database.entities.DuplicateRecordsReportItem;
import geocat.database.entities.MetadataRecord;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public interface MetadataRecordRepo extends CrudRepository<MetadataRecord, Long> {


    MetadataRecord findByEndpointJobIdAndRecordNumber(long endpointjobId, int recordNumber);

    long countByEndpointJobId(long endpointJobId);

    @Query(value = "Select count(distinct record_identifier) from metadata_record   where endpoint_job_id = ?1",
            nativeQuery = true
    )
    long countDistinctRecordIdentifierByEndpointJobId(long endpointJobId);

    @Query(value = "SELECT count(*) as count, record_identifier as recordIdentifier,text(array_agg(record_number)) as cswRecordNumbers  from metadata_record where endpoint_job_id= ?1 group by record_identifier having count(*) >1",
            nativeQuery = true)
    List<DuplicateRecordsReportItem> queryDuplicateRecordsReport(long endpointJobId);

}
