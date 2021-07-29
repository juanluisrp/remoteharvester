package net.geocat.database.linkchecker.repos2;


import net.geocat.database.linkchecker.entities2.MetadataDocument;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Scope("prototype")
public interface MetadataDocumentRepo extends Map<MetadataDocument, Long> {

    MetadataDocument findFirstByLinkCheckJobIdAndSha2(String LinkCheckJobId, String sha2);



    long countByLinkCheckJobId(String LinkCheckJobId);

    @Query(value = "Select count(*) from metadatadocument   where linkcheckjobid = ?1 and state != 'IN_PROGRESS'",
            nativeQuery = true
    )
    long countCompletedState(String LinkCheckJobId);
}