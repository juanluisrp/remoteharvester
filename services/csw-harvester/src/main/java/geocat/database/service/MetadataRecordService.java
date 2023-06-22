package geocat.database.service;

import geocat.database.entities.MetadataRecord;
import geocat.database.entities.RecordSet;
import geocat.database.repos.MetadataRecordRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class MetadataRecordService {

    @Autowired
    private MetadataRecordRepo metadataRecordRepo;

    /**
     * @param recordSet
     * @param recordNumberOffset 0=first record in the recordSet
     * @param sha2               should already exist in BlobStorage
     * @return
     */
    //idempotent
    public MetadataRecord create(RecordSet recordSet, int recordNumberOffset, String sha2, String identifier) {
        int recordNumber = recordSet.getStartRecordNumber() + recordNumberOffset;
        MetadataRecord result = metadataRecordRepo.findByEndpointJobIdAndRecordNumber(recordSet.getEndpointJobId(), recordNumber);
        if (result == null) {
            result = new MetadataRecord();
            //UUID guid = java.util.UUID.randomUUID();
            // result.setMetadataRecordId(guid.toString());
            result.setEndpointJobId(recordSet.getEndpointJobId());
        }

        result.setSha2(sha2);
        result.setRecordNumber(recordNumber);
        result.setRecordIdentifier(identifier);

        return metadataRecordRepo.save(result);
    }
}
