package net.geocat.eventprocessor.processors.processlinks;


import net.geocat.database.linkchecker.entities.LinkCheckJobState;
import net.geocat.database.linkchecker.service.LinkCheckJobService;
import net.geocat.eventprocessor.BaseEventProcessor;
import net.geocat.eventprocessor.processors.main.EventProcessor_LinksFoundInAllDocuments;
import net.geocat.events.Event;
import net.geocat.events.findlinks.LinksFoundInAllDocuments;
import net.geocat.events.processlinks.ProcessLinkEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class EventProcessor_ProcessLinkEvent extends BaseEventProcessor<ProcessLinkEvent> {

    Logger logger = LoggerFactory.getLogger( EventProcessor_ProcessLinkEvent.class);
    @Autowired
    LinkCheckJobService linkCheckJobService;

    @Override
    public EventProcessor_ProcessLinkEvent externalProcessing() throws Exception {
        return this;
    }


    @Override
    public EventProcessor_ProcessLinkEvent internalProcessing() throws Exception {
         return this;
    }


    @Override
    public List<Event> newEventProcessing() {
        List<Event> result = new ArrayList<>();

        return result;
    }
 }
