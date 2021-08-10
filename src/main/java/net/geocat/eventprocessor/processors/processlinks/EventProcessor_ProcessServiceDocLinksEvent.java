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


import net.geocat.database.linkchecker.entities.*;
import net.geocat.database.linkchecker.entities.helper.LinkState;
import net.geocat.database.linkchecker.entities.helper.ServiceMetadataDocumentState;
import net.geocat.database.linkchecker.repos.*;
import net.geocat.database.linkchecker.repos2.LinkRepo;
import net.geocat.database.linkchecker.service.LinkCheckJobService;
import net.geocat.database.linkchecker.service.LinkService;
import net.geocat.eventprocessor.BaseEventProcessor;
import net.geocat.events.Event;
import net.geocat.events.EventFactory;
import net.geocat.events.processlinks.ProcessServiceDocLinksEvent;
import net.geocat.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class EventProcessor_ProcessServiceDocLinksEvent extends BaseEventProcessor<ProcessServiceDocLinksEvent> {

    Logger logger = LoggerFactory.getLogger(EventProcessor_ProcessServiceDocLinksEvent.class);

   @Autowired
   LocalServiceMetadataRecordRepo localServiceMetadataRecordRepo;

   @Autowired
   ServiceDocumentLinkRepo serviceDocumentLinkRepo;

   @Autowired
   RetrieveServiceDocumentLink retrieveServiceDocumentLink;

   @Autowired
   RemoteServiceMetadataRecordLinkRetriever remoteServiceMetadataRecordLinkRetriever;

   @Autowired
   RemoteServiceMetadataRecordLinkRepo remoteServiceMetadataRecordLinkRepo;

   @Autowired
   CapabilitiesDatasetMetadataLinkRepo capabilitiesDatasetMetadataLinkRepo;

   @Autowired
   RetrieveCapabilitiesDatasetMetadataLink retrieveCapabilitiesDatasetMetadataLink;

   @Autowired
   RetrieveOperatesOnLink retrieveOperatesOnLink;

   @Autowired
   OperatesOnLinkRepo operatesOnLinkRepo;

   @Autowired
   CapabilitiesDocumentRepo capabilitiesDocumentRepo;

   @Autowired
    RemoteServiceMetadataRecordRepo remoteServiceMetadataRecordRepo;

   @Autowired
    CapabilitiesRemoteDatasetMetadataDocumentRepo capabilitiesRemoteDatasetMetadataDocumentRepo;

   @Autowired
   HumanReadableServiceMetadata humanReadableServiceMetadata;

   @Autowired
    OperatesOnRemoteDatasetMetadataRecordRepo operatesOnRemoteDatasetMetadataRecordRepo;

   @Autowired
   EventFactory eventFactory;

   @Autowired
           MetadataService metadataService;

    LocalServiceMetadataRecord localServiceMetadataRecord;


    boolean throwException = true;

    @Override
    public EventProcessor_ProcessServiceDocLinksEvent externalProcessing() throws Exception {
        localServiceMetadataRecord = localServiceMetadataRecordRepo.findById(getInitiatingEvent().getServiceMetadataId()).get();// make sure we re-load

        prune(); // remove any previous work (if this is being re-run)

        try {
            processDocumentLinks();
            save();
            processOperatesOnLinks();
            save();

            localServiceMetadataRecord.setState(ServiceMetadataDocumentState.LINKS_PROCESSED);
            localServiceMetadataRecord.setHumanReadable(humanReadableServiceMetadata.getHumanReadable(localServiceMetadataRecord));
            save();
        }
        catch(Exception e){
            logger.error("exception for serviceMetadataRecordId="+getInitiatingEvent().getServiceMetadataId(),e);
            localServiceMetadataRecord.setState(ServiceMetadataDocumentState.ERROR);
        }


        return this;
    }

    // for re-entry - need to clean up object (and database)
    public void prune(){
        List<CapabilitiesDocument> capDocuments = new ArrayList<>();
        List<OperatesOnRemoteDatasetMetadataRecord> opsOnDocuments = new ArrayList<>();

        //find objects and de-attach them
        for(ServiceDocumentLink link : localServiceMetadataRecord.getServiceDocumentLinks()) {
            CapabilitiesDocument capDoc = link.getCapabilitiesDocument();
            if (capDoc != null) {
                capDoc.getServiceDocumentLink().setCapabilitiesDocument(null);
                capDoc.setServiceDocumentLink(null);
                capDocuments.add(capDoc); // to be deleted
            }
        }

        for(OperatesOnLink link : localServiceMetadataRecord.getOperatesOnLinks()) {
            OperatesOnRemoteDatasetMetadataRecord record = link.getDatasetMetadataRecord();
            if (record != null) {
                record.getOperatesOnLink().setDatasetMetadataRecord(null);
                record.setOperatesOnLink(null);
                opsOnDocuments.add(record); // to be deleted
            }
        }

        if (capDocuments.isEmpty() && opsOnDocuments.isEmpty())
            return; //nothing to do

        save(); //save with objects detached

        for (CapabilitiesDocument capDoc: capDocuments){
            capabilitiesDocumentRepo.delete(capDoc);

        }
        for(OperatesOnRemoteDatasetMetadataRecord record : opsOnDocuments) {
            operatesOnRemoteDatasetMetadataRecordRepo.delete(record);
        }
    }

    public void save(){
        localServiceMetadataRecord = localServiceMetadataRecordRepo.save(localServiceMetadataRecord);
    }


    private void processOperatesOnLinks() {
        logger.debug("processing operates on links for documentid="+getInitiatingEvent().getServiceMetadataId());

        for (OperatesOnLink link : localServiceMetadataRecord.getOperatesOnLinks()) {
            handleOperatesOnLink(link);
        }
    }

    private void handleOperatesOnLink(OperatesOnLink link) {
        try {
            link = retrieveOperatesOnLink.process(link);

            link.setLinkState(LinkState.Complete);
        }
        catch(Exception e){
            logger.error("error occurred while processing ServiceMetadataDocumentId="+localServiceMetadataRecord.getServiceMetadataDocumentId()
                    +", OperatesOnLink="+link+", error="+e.getMessage());
            link.setLinkState(LinkState.ERROR);
        }
    }

    private void processDocumentLinks() {
        logger.debug("processing service document links for documentid="+getInitiatingEvent().getServiceMetadataId());
        for (ServiceDocumentLink link : localServiceMetadataRecord.getServiceDocumentLinks()) {
            handleSingleDocumentLink(link);
        }
    }

    // should retrieve a capabilities document
    // if so;
    //   a) resolve the service metadata link (in cap doc)
    //   b) for each of the layers in the cap doc, retrieve the dataset metadata link
    private ServiceDocumentLink handleSingleDocumentLink(ServiceDocumentLink link) {
        getCapabilitiesDoc(link);
        if (link.getCapabilitiesDocument() != null) {
            CapabilitiesDocument capabilitiesDocument = link.getCapabilitiesDocument();
            RemoteServiceMetadataRecordLink rsmrl = capabilitiesDocument.getRemoteServiceMetadataRecordLink();
            if (rsmrl != null) {
                getRemoteServiceMetadataRecordLink(rsmrl);
                if (rsmrl.getRemoteServiceMetadataRecord() !=null) {
                    RemoteServiceMetadataRecord remoteServiceMetadataRecord=  rsmrl.getRemoteServiceMetadataRecord();

                }
            }
            for (CapabilitiesDatasetMetadataLink capabilitiesDatasetMetadataLink : capabilitiesDocument.getCapabilitiesDatasetMetadataLinkList()) {
                handleLayerDatasetLink(capabilitiesDatasetMetadataLink);
                if (capabilitiesDatasetMetadataLink.getCapabilitiesRemoteDatasetMetadataDocument() !=null) {
                    CapabilitiesRemoteDatasetMetadataDocument capabilitiesRemoteDatasetMetadataDocument =capabilitiesDatasetMetadataLink.getCapabilitiesRemoteDatasetMetadataDocument();
                }
            }
        }
        return link;
    }

    private void handleLayerDatasetLink(CapabilitiesDatasetMetadataLink capabilitiesDatasetMetadataLink) {
        try {
            capabilitiesDatasetMetadataLink = retrieveCapabilitiesDatasetMetadataLink.process(capabilitiesDatasetMetadataLink);

            capabilitiesDatasetMetadataLink.setLinkState(LinkState.Complete);
        }
        catch(Exception e){
            logger.error("error occurred while processing ServiceMetadataDocumentId="+localServiceMetadataRecord.getServiceMetadataDocumentId()
                    +", CapabilitiesDatasetMetadataLink="+capabilitiesDatasetMetadataLink+", error="+e.getMessage(),e);
            capabilitiesDatasetMetadataLink.setLinkState(LinkState.ERROR);
        }
    }

    private void getRemoteServiceMetadataRecordLink(RemoteServiceMetadataRecordLink rsmrl) {
        try {

            rsmrl = remoteServiceMetadataRecordLinkRetriever.process(rsmrl);

            rsmrl.setLinkState(LinkState.Complete);
        }
        catch(Exception e){
            logger.error("error occurred while processing ServiceMetadataDocumentId="+localServiceMetadataRecord.getServiceMetadataDocumentId()
                    +", RemoteServiceMetadataRecordLink="+rsmrl+", error="+e.getMessage(),e);
            rsmrl.setLinkState(LinkState.ERROR);
        }
    }

    private ServiceDocumentLink getCapabilitiesDoc(ServiceDocumentLink link) {
        try {
            link = retrieveServiceDocumentLink.process(link);

            link.setLinkState(LinkState.Complete);
        }
        catch (Exception e){
            logger.error("error occurred while processing ServiceMetadataDocumentId="+localServiceMetadataRecord.getServiceMetadataDocumentId()
               +", ServiceDocumentLink="+link+", error="+e.getMessage(),e);
            link.setLinkState(LinkState.ERROR);
        }

        return link;
    }


    @Override
    public EventProcessor_ProcessServiceDocLinksEvent internalProcessing() throws Exception {


        return this;
    }


    @Override
    public List<Event> newEventProcessing() {
        List<Event> result = new ArrayList<>();

        String linkCheckJobId = getInitiatingEvent().getLinkCheckJobId();
        if (metadataService.linkProcessingComplete(linkCheckJobId))
        {
            //done
            Event e = eventFactory.createAllLinksCheckedEvent(linkCheckJobId);
            result.add(e);
        }
         return result;
    }
}
