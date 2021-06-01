package geocat.eventprocessor.processors.harvest;

import geocat.database.entities.EndpointJob;
import geocat.database.entities.EndpointJobState;
import geocat.database.entities.HarvestJobState;
import geocat.database.service.EndpointJobService;
import geocat.database.service.HarvestJobService;
import geocat.eventprocessor.BaseEventProcessor;
import geocat.events.Event;
import geocat.events.actualRecordCollection.ActualHarvestCompleted;
import geocat.events.actualRecordCollection.EndpointHarvestComplete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class EventProcessor_EndpointHarvestComplete extends BaseEventProcessor<EndpointHarvestComplete> {
    @Autowired
    EndpointJobService endpointJobService;
    @Autowired
    HarvestJobService harvestJobService;
    boolean allDone;

    public EventProcessor_EndpointHarvestComplete() {
        super();
    }

    @Override
    public EventProcessor_EndpointHarvestComplete internalProcessing() {
        List<EndpointJob> jobs = endpointJobService.findAll(getInitiatingEvent().getHarvestId());
        allDone = true;
        for (EndpointJob job : jobs) {
            boolean thisJobDone = job.getState() == EndpointJobState.RECORDS_RECEIVED;
            allDone = allDone && thisJobDone;
        }

        return this;
    }

    @Override
    public EventProcessor_EndpointHarvestComplete externalProcessing() {
        return this;
    }

    @Override
    public List<Event> newEventProcessing() {
        List<Event> result = new ArrayList<>();
        if (allDone) {
            harvestJobService.updateHarvestJobStateInDB(getInitiatingEvent().getHarvestId(), HarvestJobState.RECORDS_RECEIVED);
            ActualHarvestCompleted e = new ActualHarvestCompleted(getInitiatingEvent().getHarvestId());
            result.add(e);
        }
        return result;
    }

}