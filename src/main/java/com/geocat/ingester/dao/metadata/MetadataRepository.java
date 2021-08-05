package com.geocat.ingester.dao.metadata;

import com.geocat.ingester.model.metadata.Metadata;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@Scope("prototype")
public interface MetadataRepository extends JpaRepository<Metadata, Integer> {

    Optional<Metadata> findMetadataByUuid(String uuid);

    List<Metadata> findAllByUuidIn(Set<String> uuid);

    @Query("select m.uuid from Metadata m where m.source = :source")
    List<String> findAllUuidsBySource(String source);

    boolean existsMetadataByUuid(String uuid);

    void deleteAllByIdIn(Set<Integer> metadataIds);
}
