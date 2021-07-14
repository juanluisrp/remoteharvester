package net.geocat.eventprocessor.processors.main;

import net.geocat.database.linkchecker.entities.LinkCheckJob;
import net.geocat.database.linkchecker.entities.LinkCheckJobState;
import net.geocat.eventprocessor.BaseEventProcessor;
import net.geocat.events.Event;
import net.geocat.events.EventFactory;
import net.geocat.events.LinkCheckRequestedEvent;
import net.geocat.service.LinkCheckJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class EventProcessor_LinkCheckRequestedEvent extends BaseEventProcessor<LinkCheckRequestedEvent> {
    @Autowired
    LinkCheckJobService linkCheckJobService;

    @Autowired
    EventFactory eventFactory;


    LinkCheckJob job;


    @Override
    public EventProcessor_LinkCheckRequestedEvent externalProcessing() {
        return this;
    }


    @Override
    public EventProcessor_LinkCheckRequestedEvent internalProcessing() {
        linkCheckJobService.createLinkCheckJobInDB(getInitiatingEvent());
        job = linkCheckJobService.updateLinkCheckJobStateInDB(getInitiatingEvent().getLinkCheckJobId(), LinkCheckJobState.FINDING_LINKS);
        return this;
    }


    @Override
    public List<Event> newEventProcessing() {
        List<Event> result = new ArrayList<>();
        Event newEvent = eventFactory.createStartProcessDocumentsEvent(getInitiatingEvent());
        result.add(newEvent);
        return result;
    }
}
