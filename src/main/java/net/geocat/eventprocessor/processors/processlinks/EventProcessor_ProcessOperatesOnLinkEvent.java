/*
 *  =============================================================================
 *  ===  Copyright (C) 2021 Food and Agriculture Organization of the
 *  ===  United Nations (FAO-UN), United Nations World Food Programme (WFP)
 *  ===  and United Nations Environment Programme (UNEP)
 *  ===
 *  ===  This program is free software; you can redistribute it and/or modify
 *  ===  it under the terms of the GNU General Public License as published by
 *  ===  the Free Software Foundation; either version 2 of the License, or (at
 *  ===  your option) any later version.
 *  ===
 *  ===  This program is distributed in the hope that it will be useful, but
 *  ===  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  ===  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  ===  General Public License for more details.
 *  ===
 *  ===  You should have received a copy of the GNU General Public License
 *  ===  along with this program; if not, write to the Free Software
 *  ===  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 *  ===
 *  ===  Contact: Jeroen Ticheler - FAO - Viale delle Terme di Caracalla 2,
 *  ===  Rome - Italy. email: geonetwork@osgeo.org
 *  ===
 *  ===  Development of this program was financed by the European Union within
 *  ===  Service Contract NUMBER – 941143 – IPR – 2021 with subject matter
 *  ===  "Facilitating a sustainable evolution and maintenance of the INSPIRE
 *  ===  Geoportal", performed in the period 2021-2023.
 *  ===
 *  ===  Contact: JRC Unit B.6 Digital Economy, Via Enrico Fermi 2749,
 *  ===  21027 Ispra, Italy. email: JRC-INSPIRE-SUPPORT@ec.europa.eu
 *  ==============================================================================
 */

package net.geocat.eventprocessor.processors.processlinks;

import net.geocat.database.linkchecker.repos2.LinkRepo;
import net.geocat.database.linkchecker.service.LinkCheckJobService;
import net.geocat.database.linkchecker.service.LinkService;
import net.geocat.eventprocessor.BaseEventProcessor;
import net.geocat.events.Event;
import net.geocat.events.EventFactory;
import net.geocat.events.processlinks.ProcessOperatesOnLinkEvent;
import net.geocat.service.LinkProcessor_GetCapLinkedMetadata;
import net.geocat.service.LinkProcessor_ProcessCapDoc;
import net.geocat.service.LinkProcessor_SimpleLinkRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class EventProcessor_ProcessOperatesOnLinkEvent extends BaseEventProcessor<ProcessOperatesOnLinkEvent> {

    Logger logger = LoggerFactory.getLogger(EventProcessor_ProcessServiceDocLinkEvent.class);

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
    public EventProcessor_ProcessOperatesOnLinkEvent externalProcessing() throws Exception {
        return this;
    }


    @Override
    public EventProcessor_ProcessOperatesOnLinkEvent internalProcessing() throws Exception {

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
//        if (linkService.complete(getInitiatingEvent().getLinkCheckJobId())) {
//            linkCheckJobService.updateLinkCheckJobStateInDB(getInitiatingEvent().getLinkCheckJobId(), LinkCheckJobState.LINKS_FOUND);
//            Event e = eventFactory.createAllLinksCheckedEvent(getInitiatingEvent().getLinkCheckJobId());
//            result.add(e);
//        }
        return result;
    }

}
