package com.geocat.ingester.dao.harvester;

import com.geocat.ingester.model.harvester.RecordSet;
import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Scope("prototype")
public interface RecordSetRepo extends CrudRepository<RecordSet, String> {
    RecordSet findByHarvestJobIdAndEndpointJobIdAndStartRecordNumber(String harvestId, String endPointJobId, int start);

    List<RecordSet> findByEndpointJobId(String endPointJobId);

}

