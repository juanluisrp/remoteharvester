package net.geocat.database.linkchecker.service;

import net.geocat.database.linkchecker.entities.helper.ServiceMetadataDocumentState;
import net.geocat.database.linkchecker.entities.LocalServiceMetadataRecord;
import net.geocat.xml.XmlServiceRecordDoc;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ServiceMetadataRecordService {



    public LocalServiceMetadataRecord create(XmlServiceRecordDoc doc,
                                             Long underlyingHarvestMetadataRecordId,
                                             String linkCheckJobId,
                                             String sha2){
        LocalServiceMetadataRecord result = new LocalServiceMetadataRecord();

        result.setHarvesterMetadataRecordId(underlyingHarvestMetadataRecordId);
        result.setLinkCheckJobId(linkCheckJobId);
        result.setSha2(sha2);

        //metadataDocumentFactory.augment(result,doc);

        result.setFileIdentifier(doc.getFileIdentifier());
        result.setMetadataRecordType(doc.getMetadataDocumentType()); // service
        result.setMetadataServiceType(doc.getServiceType());

        result.setState(ServiceMetadataDocumentState.CREATED);

        return result;
    }
}
