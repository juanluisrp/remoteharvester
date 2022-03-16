package geocat.service;

import geocat.database.entities.HarvestJob;
import geocat.database.service.HarvestJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Scope("prototype")
public class GetHarvestInfoService {

    @Autowired
    HarvestJobService harvestJobService;

    public String getLastCompletedHarvestJobIdByLongTermTag(String longTermTag) {
        String harvestJobId = "";

        Optional<HarvestJob> harvestJob = harvestJobService.getLastCompletedHarvestJobIdByLongTermTag(longTermTag);

        if (harvestJob.isPresent()) {
            harvestJobId = harvestJob.get().getJobId();
        }

        return harvestJobId;
    }
}
