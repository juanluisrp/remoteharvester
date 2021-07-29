package net.geocat.database.linkchecker.service;

import net.geocat.database.linkchecker.entities.CapabilitiesDatasetMetadataLink;
import net.geocat.database.linkchecker.entities.CapabilitiesRemoteDatasetMetadataDocument;
import net.geocat.database.linkchecker.entities.OperatesOnLink;
import net.geocat.database.linkchecker.entities.OperatesOnRemoteDatasetMetadataRecord;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class RemoteDatasetMetadataRecordService {

    public OperatesOnRemoteDatasetMetadataRecord createRemoteDatasetMetadataRecord(OperatesOnLink link){
        OperatesOnRemoteDatasetMetadataRecord result = new OperatesOnRemoteDatasetMetadataRecord();
        return result;
    }

    public CapabilitiesRemoteDatasetMetadataDocument createCapabilitiesRemoteDatasetMetadataDocument(CapabilitiesDatasetMetadataLink link){
        CapabilitiesRemoteDatasetMetadataDocument result = new CapabilitiesRemoteDatasetMetadataDocument();
        return result;
    }
}
