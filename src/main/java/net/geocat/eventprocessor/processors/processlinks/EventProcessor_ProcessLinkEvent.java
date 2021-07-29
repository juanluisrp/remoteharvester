package net.geocat.eventprocessor.processors.processlinks;


import net.geocat.database.linkchecker.entities2.Link;
import net.geocat.database.linkchecker.entities2.LinkCheckJobState;
import net.geocat.database.linkchecker.entities2.LinkState;
import net.geocat.database.linkchecker.repos2.LinkRepo;
import net.geocat.database.linkchecker.service.LinkCheckJobService;
import net.geocat.database.linkchecker.service.LinkService;
import net.geocat.eventprocessor.BaseEventProcessor;
import net.geocat.events.Event;
import net.geocat.events.EventFactory;
import net.geocat.events.processlinks.ProcessLinkEvent;
import net.geocat.service.LinkProcessor_GetCapLinkedMetadata;
import net.geocat.service.LinkProcessor_ProcessCapDoc;
import net.geocat.service.LinkProcessor_SimpleLinkRequest;
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
    LinkProcessor_SimpleLinkRequest linkProcessor_simpleLinkRequest;

    @Autowired
    LinkProcessor_ProcessCapDoc linkProcessor_processCapDoc;

    @Autowired
    LinkProcessor_GetCapLinkedMetadata linkProcessor_getCapLinkedMetadata;

    @Autowired
    LinkCheckJobService linkCheckJobService;

    @Autowired
    LinkRepo linkRepo;

    @Autowired
    LinkService linkService;

    @Autowired
    EventFactory eventFactory;

    @Override
    public EventProcessor_ProcessLinkEvent externalProcessing() throws Exception {
        return this;
    }


    @Override
    public EventProcessor_ProcessLinkEvent internalProcessing() throws Exception {

//        Link link = linkRepo.findById(this.getInitiatingEvent().getLinkId()).get();
//        link.setLinkState(LinkState.IN_PROGRESS);
//        linkRepo.save(link);
//        try {
//
//            link = linkProcessor_simpleLinkRequest.process(link);
//            linkRepo.save(link);
//
//            link = linkProcessor_processCapDoc.process(link);
//            linkRepo.save(link);
//
//            link = linkProcessor_getCapLinkedMetadata.process(link);
//
//            link.setLinkState(LinkState.COMPLETE);
//            linkRepo.save(link);
//        }
//        catch (Exception e){
//            link.setLinkState(LinkState.ERROR);
//            link.setLinkErrorMessage(e.getMessage());
//            linkRepo.save(link);
//            logger.error("error occurred processing link "+link.getLinkId(), e);
//            throw e;
//        }

         return this;
    }


    @Override
    public List<Event> newEventProcessing() {
        List<Event> result = new ArrayList<>();
        if (linkService.complete(getInitiatingEvent().getLinkCheckJobId())){
            linkCheckJobService.updateLinkCheckJobStateInDB(getInitiatingEvent().getLinkCheckJobId(),LinkCheckJobState.LINKS_FOUND);
            Event e = eventFactory.createAllLinksCheckedEvent(getInitiatingEvent().getLinkCheckJobId());
            result.add(e);
        }
        return result;
    }
 }
