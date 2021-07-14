package net.geocat.eventprocessor.processors.findlinks;

import net.geocat.eventprocessor.BaseEventProcessor;
import net.geocat.events.Event;
import net.geocat.events.findlinks.StartProcessDocumentsEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class EventProcessor_StartProcessDocumentsEvent extends BaseEventProcessor<StartProcessDocumentsEvent> {


    @Override
    public EventProcessor_StartProcessDocumentsEvent externalProcessing() {
        return this;
    }


    @Override
    public EventProcessor_StartProcessDocumentsEvent internalProcessing() {
                return this;
    }


    @Override
    public List<Event> newEventProcessing() {
        List<Event> result = new ArrayList<>();

        return result;
    }
}
