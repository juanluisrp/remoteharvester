package geocat.database.service;

import geocat.database.entities.EndpointJob;
import geocat.database.entities.HarvestJob;
import geocat.database.repos.EndpointJobRepo;
import geocat.database.repos.HarvestJobRepo;
import geocat.events.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Scope("prototype")
public class EndpointJobService {

    @Autowired
    private EndpointJobRepo endpointJobRepo;

    @Autowired
    private HarvestJobRepo harvestJobRepo;





    public EndpointJob createInitial(String harvestId, String url, String filter, boolean lookForNestedDiscoveryService)
    {
        EndpointJob job = endpointJobRepo.findFirstByHarvestJobIdAndUrl(harvestId,url);
        if (job == null) //normal case
        {
            UUID guid = java.util.UUID.randomUUID();

            EndpointJob newJob = new EndpointJob();
            newJob.setEndpointJobId(guid.toString());
            newJob.setHarvestJobId(harvestId);
            newJob.setUrl(url);
            newJob.setFilter(filter);
            newJob.setState("CREATED");
            newJob.setLookForNestedDiscoveryService(lookForNestedDiscoveryService);
            return endpointJobRepo.save(newJob);
        }
        //already in there, no need to add another one
        return job;
    }

//    public EndpointJob addGetRecordsUrl(String id, String url){
//        EndpointJob job = endpointJobRepo.findById(id).get();
//        job.setUrlGetRecords(url);
//        return endpointJobRepo.save(job);
//    }

//    public String getGetRecordsUrl(String id){
//        EndpointJob job = endpointJobRepo.findById(id).get();
//        return job.getUrlGetRecords();
//    }

//    public EndpointJob addExpectedRecords(String endPointId, int nrecords) {
//        EndpointJob job = endpointJobRepo.findById(endPointId).get();
//        job.setExpectedNumberOfRecords(nrecords);
//        return endpointJobRepo.save(job);
//    }

    public boolean areTheseUrlsInDB(String harvestId, List<String> urls){
        return  !endpointJobRepo.findByHarvestJobIdAndUrlIn(harvestId,urls).isEmpty();
    }

    public EndpointJob updateState(String endpointId, String state){
        EndpointJob job = getById(endpointId);
        job.setState(state);
        return endpointJobRepo.save(job);
    }

    public List<EndpointJob> findAll(String harvestId){
        return endpointJobRepo.findByHarvestJobId(harvestId);
    }

    public EndpointJob getById(String id){
        return endpointJobRepo.findById(id).get();
    }

}
