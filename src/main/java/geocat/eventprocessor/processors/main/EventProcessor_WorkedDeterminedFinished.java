package geocat.eventprocessor.processors.main;

import geocat.eventprocessor.BaseEventProcessor;
import geocat.events.Event;
import geocat.events.actualRecordCollection.ActualHarvestStartCommand;
import geocat.events.determinework.WorkedDeterminedFinished;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class EventProcessor_WorkedDeterminedFinished extends BaseEventProcessor<WorkedDeterminedFinished>
{

    @Override
    public EventProcessor_WorkedDeterminedFinished internalProcessing(){
        return this;
    }

    @Override
    public EventProcessor_WorkedDeterminedFinished externalProcessing(){
        return this;
    }

    @Override
    public List<Event> newEventProcessing(){
        List<Event> result = new ArrayList<>();
        ActualHarvestStartCommand e = new ActualHarvestStartCommand(getInitiatingEvent().getHarvestId());
        result.add(e);
        return result;
    }

}