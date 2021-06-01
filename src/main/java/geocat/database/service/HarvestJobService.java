package geocat.database.service;

import geocat.database.entities.EndpointJob;
import geocat.database.entities.HarvestJob;
import geocat.database.repos.EndpointJobRepo;
import geocat.database.repos.HarvestJobRepo;
import geocat.events.HarvestRequestedEvent;
import geocat.events.determinework.WorkedDeterminedFinished;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Scope("prototype")
public class HarvestJobService {

    @Autowired
    private HarvestJobRepo harvestJobRepo;
    @Autowired
    private EndpointJobRepo endpointJobRepo;

    //idempotent tx
    public HarvestJob createNewHarvestJobInDB(HarvestRequestedEvent event) {
        Optional<HarvestJob> job = harvestJobRepo.findById(event.getHarvestId());

        if (job.isPresent()) //2nd attempt
        {
            job.get().setState("CREATING");
            return harvestJobRepo.save(job.get());
        }
        HarvestJob newJob = new HarvestJob();
        newJob.setJobId(event.getHarvestId());
        newJob.setFilter(event.getFilter());
        newJob.setLookForNestedDiscoveryService(event.isLookForNestedDiscoveryService());
        newJob.setInitialUrl(event.getUrl());
        newJob.setLongTermTag(event.getLongTermTag());
        newJob.setState("CREATING");
        newJob.setMessages("hi");

        return harvestJobRepo.save(newJob);
    }

    public HarvestJob updateHarvestJobStateInDB(String guid, String state) {
        HarvestJob job = harvestJobRepo.findById(guid).get();
        job.setState(state);
        return harvestJobRepo.save(job);
    }

    public synchronized WorkedDeterminedFinished determineIfWorkCompleted(String harvestId) {
        HarvestJob harvestJob = harvestJobRepo.findById(harvestId).get();
        if (!harvestJob.getState().equalsIgnoreCase("DETERMINEWORK"))
            return null; //already completed earlier
        List<EndpointJob> outstandingJobs = endpointJobRepo.findByHarvestJobIdAndState(harvestId, "CREATED");
        boolean workCompleted = outstandingJobs.isEmpty();
        if (workCompleted) {
            //move state
            updateHarvestJobStateInDB(harvestId, "WORKDETERMINED");
            return new WorkedDeterminedFinished(harvestId);
        }
        return null;
    }

    public HarvestJob getById(String id) {
        return harvestJobRepo.findById(id).get();
    }

    public List<EndpointJob> getEndpointJobs(String harvestId) {
        return endpointJobRepo.findByHarvestJobId(harvestId);
    }

}
