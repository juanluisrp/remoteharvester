package com.geocat.ingester.dao.harvester;

import com.geocat.ingester.model.harvester.EndpointJob;
import com.geocat.ingester.model.harvester.EndpointJobState;
import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public interface EndpointJobRepo extends CrudRepository<EndpointJob, String> {

    EndpointJob findFirstByHarvestJobIdAndUrl(String harvestId, String url);

    List<EndpointJob> findByHarvestJobIdAndUrlIn(String harvestId, List<String> urls);

    List<EndpointJob> findByHarvestJobIdAndStateNot(String harvestId, EndpointJobState not_state);

    List<EndpointJob> findByHarvestJobIdAndState(String harvestId, EndpointJobState state);

    List<EndpointJob> findByHarvestJobId(String harvestId);


}
