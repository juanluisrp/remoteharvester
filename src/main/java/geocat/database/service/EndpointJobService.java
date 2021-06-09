package geocat.database.service;

import geocat.database.entities.EndpointJob;
import geocat.database.entities.EndpointJobState;
import geocat.database.repos.EndpointJobRepo;
import geocat.database.repos.HarvestJobRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Scope("prototype")
public class EndpointJobService {

    @Autowired
    private EndpointJobRepo endpointJobRepo;

    @Autowired
    private HarvestJobRepo harvestJobRepo;

    //idempotent
    public EndpointJob createInitial(String harvestId, String url, String filter, boolean lookForNestedDiscoveryService) {
        EndpointJob job = endpointJobRepo.findFirstByHarvestJobIdAndUrl(harvestId, url);
        if (job == null) //normal case
        {
            UUID guid = java.util.UUID.randomUUID();

            EndpointJob newJob = new EndpointJob();
            newJob.setEndpointJobId(guid.toString());
            newJob.setHarvestJobId(harvestId);
            newJob.setUrl(url);
            newJob.setFilter(filter);
            newJob.setState(EndpointJobState.CREATING);
            newJob.setLookForNestedDiscoveryService(lookForNestedDiscoveryService);
            return endpointJobRepo.save(newJob);
        }
        //already in there, no need to add another one
        return job;
    }

    public boolean areTheseUrlsInDB(String harvestId, List<String> urls) {
        return !endpointJobRepo.findByHarvestJobIdAndUrlIn(harvestId, urls).isEmpty();
    }

    public EndpointJob updateState(String endpointId, EndpointJobState state) {
        EndpointJob job = getById(endpointId);
        job.setState(state);
        return endpointJobRepo.save(job);
    }

    public List<EndpointJob> findAll(String harvestId) {
        return endpointJobRepo.findByHarvestJobId(harvestId);
    }

    public EndpointJob getById(String id) {
        return endpointJobRepo.findById(id).get();
    }

}
