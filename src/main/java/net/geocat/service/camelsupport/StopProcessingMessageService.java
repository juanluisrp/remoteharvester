package net.geocat.service.camelsupport;


import net.geocat.database.linkchecker.entities.LinkCheckJob;
import net.geocat.database.linkchecker.entities.LinkCheckJobState;
import net.geocat.database.linkchecker.repos.LinkCheckJobRepo;
import org.apache.camel.Exchange;
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
    LinkCheckJobRepo linkCheckJobRepo;

    public void checkIfShouldBeProcessed(Exchange exchange) {
        String processId = (String) exchange.getMessage().getHeader("processID");
        Optional<LinkCheckJob> job = linkCheckJobRepo.findById(processId);
        if (!job.isPresent())
            return; // likely first message - cannot see in DB yet
        if ((job.get().getState() == LinkCheckJobState.ERROR) || (job.get().getState() == LinkCheckJobState.USERABORT)) {
            logger.debug("processID=" + job.get().getJobId() + " is in state " + job.get().getState().toString() + ", no processing for this message");
            exchange.setRouteStop(true);
        }
    }
}
