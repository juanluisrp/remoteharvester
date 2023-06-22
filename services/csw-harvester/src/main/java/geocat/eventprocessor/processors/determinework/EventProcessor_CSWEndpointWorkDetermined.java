package geocat.eventprocessor.processors.determinework;

import geocat.database.entities.EndpointJobState;
import geocat.database.service.EndpointJobService;
import geocat.database.service.HarvestJobService;
import geocat.eventprocessor.BaseEventProcessor;
import geocat.events.Event;
import geocat.events.determinework.CSWEndpointWorkDetermined;
import geocat.events.determinework.WorkedDeterminedFinished;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class EventProcessor_CSWEndpointWorkDetermined extends BaseEventProcessor<CSWEndpointWorkDetermined> {

    @Autowired
    HarvestJobService harvestJobService;

    @Autowired
    EndpointJobService endpointJobService;


    @Override
    public EventProcessor_CSWEndpointWorkDetermined externalProcessing() {
        return this;
    }


    @Override
    public EventProcessor_CSWEndpointWorkDetermined internalProcessing() {
        endpointJobService.updateState(getInitiatingEvent().getEndPointId(), EndpointJobState.WORK_DETERMINED);
        return this;
    }


    @Override
    public List<Event> newEventProcessing() {
        List<Event> result = new ArrayList<>();
        WorkedDeterminedFinished finished = harvestJobService.determineIfWorkCompleted(getInitiatingEvent().getHarvesterId());
        if (finished != null)
            result.add(finished);
        return result;
    }

}