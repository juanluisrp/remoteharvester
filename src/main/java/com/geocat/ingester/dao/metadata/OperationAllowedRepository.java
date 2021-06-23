package com.geocat.ingester.dao.metadata;

import com.geocat.ingester.model.metadata.OperationAllowed;
import com.geocat.ingester.model.metadata.OperationAllowedId;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@Scope("prototype")
public interface OperationAllowedRepository extends PagingAndSortingRepository<OperationAllowed, OperationAllowedId> {

    @Query("select oa from OperationAllowed oa where oa.id.metadataId = :metadataId")
    public List<OperationAllowed> findAllByMetadataId(@Param("metadataId") Integer metadataId);

    @Query("select oa from OperationAllowed oa where oa.id.metadataId IN :metadataIds and oa.id.groupId = 1 and oa.id.operationId = 0")
    public List<OperationAllowed> findPublicOperationAllowedByMetadataIds(@Param("metadataIds") Set<Integer> metadataIds);
}
