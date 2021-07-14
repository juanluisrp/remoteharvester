package net.geocat.database.linkchecker.service;

import net.geocat.database.harvester.repos.MetadataRecordRepo;
import net.geocat.database.linkchecker.entities.MetadataDocument;
import net.geocat.database.linkchecker.entities.MetadataDocumentState;
import net.geocat.database.linkchecker.repos.MetadataDocumentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class MetadataDocumentService {

    @Autowired
    public MetadataDocumentRepo metadataDocumentRepo;

    public MetadataDocument create(String linkCheckJobId, String sha2, Long harvesterMetadataRecordId){
        MetadataDocument doc = metadataDocumentRepo.findFirstByLinkCheckJobIdAndSha2(linkCheckJobId,sha2);
        if (doc != null)
            return doc;
        doc = new MetadataDocument();
        doc.setLinkCheckJobId(linkCheckJobId);
        doc.setSha2(sha2);
        doc.setHarvesterMetadataRecordId(harvesterMetadataRecordId);
        doc.setState(MetadataDocumentState.IN_PROGRESS);
        return metadataDocumentRepo.save(doc);
    }

    public MetadataDocument find(String linkCheckJobId, String sha2)
    {
        MetadataDocument doc = metadataDocumentRepo.findFirstByLinkCheckJobIdAndSha2(linkCheckJobId,sha2);
        return doc;
    }

//    public MetadataDocument setState(long metadataDocumentId, MetadataDocumentState state){
//        MetadataDocument doc = metadataDocumentRepo.findById(metadataDocumentId).get();
//        doc.setState(state);
//        return metadataDocumentRepo.save(doc);
//    }

    public MetadataDocument setState(MetadataDocument doc, MetadataDocumentState state){
       // MetadataDocument doc = metadataDocumentRepo.findById(metadataDocumentId).get();
        doc.setState(state);
        return metadataDocumentRepo.save(doc);
    }

    public boolean complete(String linkCheckJobId) {
        long nrecords = metadataDocumentRepo.countByLinkCheckJobId(linkCheckJobId);
        long nrecordsComplete = metadataDocumentRepo.countCompletedState(linkCheckJobId);
        return nrecords == nrecordsComplete;
    }
}
