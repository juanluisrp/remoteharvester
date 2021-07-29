package net.geocat.eventprocessor.processors.main;

import net.geocat.database.linkchecker.entities2.Link;
import net.geocat.database.linkchecker.entities2.LinkCheckJobState;
import net.geocat.database.linkchecker.service.LinkCheckJobService;
import net.geocat.database.linkchecker.service.LinkService;
import net.geocat.eventprocessor.BaseEventProcessor;
 import net.geocat.events.Event;
import net.geocat.events.EventFactory;
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
public class EventProcessor_LinksFoundInAllDocuments extends BaseEventProcessor<LinksFoundInAllDocuments> {

    Logger logger = LoggerFactory.getLogger(EventProcessor_LinksFoundInAllDocuments.class);


    @Autowired
    LinkCheckJobService linkCheckJobService;

    @Autowired
    LinkService linkService;

    @Autowired
    EventFactory eventFactory;

    @Override
    public EventProcessor_LinksFoundInAllDocuments externalProcessing() throws Exception {
        return this;
    }


    @Override
    public EventProcessor_LinksFoundInAllDocuments internalProcessing() throws Exception {
        linkCheckJobService.updateLinkCheckJobStateInDB(getInitiatingEvent().getLinkCheckJobId(), LinkCheckJobState.LINKS_FOUND);
        return this;
    }


    @Override
    public List<Event> newEventProcessing() {
        List<Event> result = new ArrayList<>();
        linkCheckJobService.updateLinkCheckJobStateInDB(getInitiatingEvent().getLinkCheckJobId(), LinkCheckJobState.CHECKING_LINKS);

        List<Link> links = linkService.findLinks(getInitiatingEvent().getLinkCheckJobId());
        for(Link link : links){
            ProcessLinkEvent e = eventFactory.createProcessLinkEvent(link);
            result.add(e);
        }

        return result;
    }
}