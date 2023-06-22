package geocat.eventprocessor.processors.main;

import geocat.database.entities.HarvestJobState;
import geocat.database.service.HarvestJobService;
import geocat.eventprocessor.BaseEventProcessor;
import geocat.events.Event;
import geocat.events.actualRecordCollection.ActualHarvestCompleted;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class EventProcessor_ActualHarvestCompleted extends BaseEventProcessor<ActualHarvestCompleted> {

    @Autowired
    HarvestJobService harvestJobService;


    @Override
    public EventProcessor_ActualHarvestCompleted externalProcessing() {
        harvestJobService.updateHarvestJobStateInDB(getInitiatingEvent().getHarvestId(), HarvestJobState.COMPLETE);
        return this;
    }


    @Override
    public EventProcessor_ActualHarvestCompleted internalProcessing() {
        return this;
    }


    @Override
    public List<Event> newEventProcessing() {
        return new ArrayList<>();
    }

}