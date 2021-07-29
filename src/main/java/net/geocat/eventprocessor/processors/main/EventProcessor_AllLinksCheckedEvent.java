package net.geocat.eventprocessor.processors.main;


import net.geocat.database.linkchecker.entities2.LinkCheckJobState;
import net.geocat.database.linkchecker.service.LinkCheckJobService;
import net.geocat.eventprocessor.BaseEventProcessor;
import net.geocat.events.Event;
import net.geocat.events.processlinks.AllLinksCheckedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class EventProcessor_AllLinksCheckedEvent extends BaseEventProcessor<AllLinksCheckedEvent> {

    Logger logger = LoggerFactory.getLogger( net.geocat.eventprocessor.processors.processlinks.EventProcessor_ProcessLinkEvent.class);

    @Autowired
    LinkCheckJobService linkCheckJobService;

    @Override
    public EventProcessor_AllLinksCheckedEvent externalProcessing() {
        return this;
    }


    @Override
    public EventProcessor_AllLinksCheckedEvent internalProcessing() {
         linkCheckJobService.updateLinkCheckJobStateInDB(getInitiatingEvent().getLinkCheckJobId(), LinkCheckJobState.COMPLETE);
          return this;
    }


    @Override
    public List<Event> newEventProcessing() {
        List<Event> result = new ArrayList<>();

        return result;
    }

}
