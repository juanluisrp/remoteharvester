package net.geocat.eventprocessor.processors.main;

import net.geocat.database.orchestrator.entities.OrchestratedHarvestProcess;
import net.geocat.database.orchestrator.entities.OrchestratedHarvestProcessState;
import net.geocat.database.orchestrator.repos.OrchestratedHarvestProcessRepo;
import net.geocat.eventprocessor.BaseEventProcessor;
import net.geocat.events.Event;
import net.geocat.events.OrchestratedHarvestAbortEvent;
import net.geocat.events.OrchestratedHarvestRequestedEvent;
import net.geocat.model.HarvestStartResponse;
import net.geocat.service.OrchestratedHarvestProcessService;
import net.geocat.service.OrchestratedHarvestService;
import net.geocat.service.ProcessLockingService;
import net.geocat.service.exernalservices.HarvesterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;


@Component
@Scope("prototype")
public class EventProcessor_OrchestratedHarvestRequestedEvent extends BaseEventProcessor<OrchestratedHarvestRequestedEvent> {

    Logger logger = LoggerFactory.getLogger( EventProcessor_OrchestratedHarvestAbortEvent.class);

    @Autowired
    OrchestratedHarvestProcessRepo orchestratedHarvestProcessRepo;

    @Autowired
    OrchestratedHarvestProcessService orchestratedHarvestProcessService;

    @Autowired
    OrchestratedHarvestService orchestratedHarvestService;

    @Autowired
    ProcessLockingService processLockingService;

    @Autowired
    HarvesterService harvesterService;

    OrchestratedHarvestProcess job;

    @Override
    public EventProcessor_OrchestratedHarvestRequestedEvent externalProcessing() throws Exception {
        String processID = getInitiatingEvent().getProcessId();
        Lock lock = processLockingService.getLock(processID);
        try {
            lock.lock();
            job = orchestratedHarvestService.createOrchestratedHarvestProcess(processID);
            job.setExecuteLinkChecker(getInitiatingEvent().getHarvesterConfig().getExecuteLinkChecker());
            job.setSkipHarvesting(getInitiatingEvent().getHarvesterConfig().getSkipHarvesting());
            // Set to null to avoid sending it to the harvester component
            getInitiatingEvent().getHarvesterConfig().setExecuteLinkChecker(null);

            if (!job.getSkipHarvesting()) {
                HarvestStartResponse harvestStartResponse = harvesterService.startHarvest(getInitiatingEvent().getHarvesterConfig());
                job.setHarvesterJobId(harvestStartResponse.getProcessID());

            } else {
                // Get the last completed harvest job for the longTermTag
                String harvestJobId = harvesterService.getLastCompletedHarvestJobIdByLongTermTag(getInitiatingEvent().getHarvesterConfig());

                if (StringUtils.hasLength(harvestJobId)) {
                    job.setHarvesterJobId(harvestJobId);
                } else {
                    // Trigger the harvester as there is no completed harvester job
                    job.setSkipHarvesting(false);

                    HarvestStartResponse harvestStartResponse = harvesterService.startHarvest(getInitiatingEvent().getHarvesterConfig());
                    job.setHarvesterJobId(harvestStartResponse.getProcessID());
                }
            }
            orchestratedHarvestProcessRepo.save(job);
            job = orchestratedHarvestProcessService.updateLinkCheckJobStateInDB(processID,OrchestratedHarvestProcessState.HAVESTING);
        }
        finally {
            lock.unlock();
        }
        return this;
    }


    @Override
    public EventProcessor_OrchestratedHarvestRequestedEvent internalProcessing() {

        return this;
    }


    @Override
    public List<Event> newEventProcessing() {
        List<Event> result = new ArrayList<>();

        return result;
    }

}
