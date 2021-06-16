package geocat.service;

import geocat.database.entities.EndpointJob;
import geocat.database.entities.HarvestJob;
import geocat.database.repos.MetadataRecordRepo;
import geocat.database.service.EndpointJobService;
import geocat.database.service.HarvestJobService;
import geocat.model.EndpointStatus;
import geocat.model.HarvestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class GetStatusService {

    @Autowired
    HarvestJobService harvestJobService;

    @Autowired
    EndpointJobService endpointJobService;

    @Autowired
    MetadataRecordRepo metadataRecordRepo;

    public HarvestStatus getStatus(String processId) {
        HarvestJob job = harvestJobService.getById(processId);
        List<EndpointJob> endpointJobs = endpointJobService.findAll(processId);

        HarvestStatus result = new HarvestStatus(job);
        for (EndpointJob endpointJob : endpointJobs) {
            long numberReceived = computeNumberReceived(endpointJob);
            result.endpoints.add(new EndpointStatus(endpointJob, (int) numberReceived));
        }
        return result;
    }

    private long computeNumberReceived(EndpointJob endpointJob) {
        return metadataRecordRepo.countByEndpointJobId(endpointJob.getEndpointJobId());
    }
}
