package geocat.service;

import geocat.csw.CSWService;
import geocat.database.entities.HarvestJob;
import geocat.database.entities.HarvestJobState;
import geocat.database.repos.HarvestJobRepo;
import geocat.database.service.HarvestJobService;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Scope("prototype")
public class StopProcessingMessageService {

    Logger logger = LoggerFactory.getLogger(StopProcessingMessageService.class);


    @Autowired
    HarvestJobRepo harvestJobRepo;

    public void checkIfShouldBeProcessed(Exchange exchange) {
        String processId = (String) exchange.getMessage().getHeader("processID");
        Optional<HarvestJob> job = harvestJobRepo.findById(processId);
        if (!job.isPresent())
            return; // likely first message - cannot see in DB yet
        if ( (job.get().getState() == HarvestJobState.ERROR) || (job.get().getState() == HarvestJobState.USERABORT) ) {
            logger.debug("processID="+job.get().getJobId()+" is in state "+job.get().getState().toString()+", no processing for this message");
            exchange.setRouteStop(true);
        }
    }
}
