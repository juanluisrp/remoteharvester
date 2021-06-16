package geocat.eventprocessor.processors.main;

import geocat.database.entities.HarvestJob;
import geocat.database.entities.HarvestJobState;
import geocat.database.service.HarvestJobService;
import geocat.eventprocessor.BaseEventProcessor;
import geocat.eventprocessor.processors.harvest.EventProcessor_GetRecordsCommand;
import geocat.events.Event;
import geocat.events.HarvestAbortEvent;
import geocat.events.HarvestRequestedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class EventProcessor_HarvestAbortEvent    extends BaseEventProcessor<HarvestAbortEvent> {

    Logger logger = LoggerFactory.getLogger(EventProcessor_HarvestAbortEvent.class);


    @Autowired
    HarvestJobService harvestJobService;

    @Override
    public EventProcessor_HarvestAbortEvent externalProcessing() {
            return this;
        }


    @Override
    public EventProcessor_HarvestAbortEvent internalProcessing() {
        String processID = getInitiatingEvent().getProcessID();
        logger.warn("attempting to user abort for "+processID);
        harvestJobService.updateHarvestJobStateInDB(processID, HarvestJobState.USERABORT);
        logger.warn("user abort processed for "+processID);
        return this;
    }


    @Override
    public List<Event> newEventProcessing() {
        List<Event> result = new ArrayList<>();
        return result;
    }

}
