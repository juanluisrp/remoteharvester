package net.geocat.database.linkchecker.repos;


import net.geocat.database.linkchecker.entities.MetadataDocument;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface MetadataDocumentRepo extends CrudRepository<MetadataDocument, Long> {

    MetadataDocument findFirstByLinkCheckJobIdAndSha2(String LinkCheckJobId, String sha2);



    long countByLinkCheckJobId(String LinkCheckJobId);

    @Query(value = "Select count(*) from metadatadocument   where linkcheckjobid = ?1 and state != 'IN_PROGRESS'",
            nativeQuery = true
    )
    long countCompletedState(String LinkCheckJobId);
}