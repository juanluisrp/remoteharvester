package net.geocat.eventprocessor.processors.main;

import net.geocat.database.orchestrator.entities.OrchestratedHarvestProcessState;
import net.geocat.database.orchestrator.repos.OrchestratedHarvestProcessRepo;
import net.geocat.eventprocessor.BaseEventProcessor;
import net.geocat.events.Event;
import net.geocat.events.OrchestratedHarvestAbortEvent;
import net.geocat.service.OrchestratedHarvestProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@Scope("prototype")
public class EventProcessor_OrchestratedHarvestAbortEvent extends BaseEventProcessor<OrchestratedHarvestAbortEvent> {

    Logger logger = LoggerFactory.getLogger( EventProcessor_OrchestratedHarvestAbortEvent.class);

    @Autowired
    OrchestratedHarvestProcessRepo orchestratedHarvestProcessRepo;

    @Autowired
    OrchestratedHarvestProcessService orchestratedHarvestProcessService;


    @Override
    public EventProcessor_OrchestratedHarvestAbortEvent externalProcessing() {
        return this;
    }


    @Override
    public EventProcessor_OrchestratedHarvestAbortEvent internalProcessing() {
        String processID = getInitiatingEvent().getProcessID();
        logger.warn("attempting to user abort for " + processID);
        orchestratedHarvestProcessService.updateLinkCheckJobStateInDB(processID, OrchestratedHarvestProcessState.USERABORT);
        logger.warn("user abort processed for " + processID);
        return this;
     }


    @Override
    public List<Event> newEventProcessing() {
        List<Event> result = new ArrayList<>();

        return result;
    }

}
