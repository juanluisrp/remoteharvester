package geocat.eventprocessor.processors.determinework;

import geocat.database.entities.EndpointJob;
import geocat.database.service.EndpointJobService;
import geocat.eventprocessor.BaseEventProcessor;
import geocat.events.Event;
import geocat.events.EventFactory;
import geocat.events.determinework.DetermineWorkStartCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class EventProcessor_DetermineWorkStartCommand extends BaseEventProcessor<DetermineWorkStartCommand> {

    @Autowired
    EndpointJobService endpointJobService;

    @Autowired
    EventFactory eventFactory;


    EndpointJob job;


    @Override
    public EventProcessor_DetermineWorkStartCommand externalProcessing() {
        return this;
    }


    @Override
    public EventProcessor_DetermineWorkStartCommand internalProcessing() {

        job = endpointJobService.createInitial(getInitiatingEvent().getHarvestId(),
                getInitiatingEvent().getInitialUrl(),
                getInitiatingEvent().getFilter(),
                getInitiatingEvent().isLookForNestedDiscoveryService());


        return this;
    }

    @Override
    public List<Event> newEventProcessing() {
        List<Event> result = new ArrayList<>();


        Event newEvent = eventFactory.create_CSWEndPointDetectedEvent(
                job.getHarvestJobId(),
                job.getEndpointJobId(),
                job.getUrl(),
                job.getFilter(),
                job.isLookForNestedDiscoveryService());

        result.add(newEvent);
        return result;
    }

}