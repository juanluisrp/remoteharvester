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

package net.geocat.eventprocessor.processors.findlinks;


import net.geocat.database.linkchecker.entities.LocalServiceMetadataRecord;
import net.geocat.database.linkchecker.entities.OperatesOnLink;
import net.geocat.database.linkchecker.entities.ServiceDocumentLink;
import net.geocat.database.linkchecker.entities.helper.ServiceMetadataDocumentState;
import net.geocat.database.linkchecker.entities2.MetadataDocumentState;
import net.geocat.database.linkchecker.repos2.LinkRepo;
import net.geocat.database.linkchecker.repos2.MetadataDocumentRepo;
import net.geocat.database.linkchecker.service.MetadataDocumentService;
import net.geocat.eventprocessor.BaseEventProcessor;
import net.geocat.events.Event;
import net.geocat.events.EventFactory;
import net.geocat.events.findlinks.LinksFoundInAllDocuments;
import net.geocat.events.findlinks.ProcessServiceMetadataDocumentEvent;
import net.geocat.service.BlobStorageService;
import net.geocat.service.LinkFactory;
import net.geocat.service.ServiceDocLinkExtractor;
import net.geocat.xml.XmlDoc;
import net.geocat.xml.XmlDocumentFactory;
import net.geocat.xml.XmlMetadataDocument;
import net.geocat.xml.XmlServiceRecordDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class EventProcessor_ProcessServiceMetadataDocumentEvent extends BaseEventProcessor<ProcessServiceMetadataDocumentEvent> {

    Logger logger = LoggerFactory.getLogger(ProcessServiceMetadataDocumentEvent.class);


    @Autowired
    EventFactory eventFactory;

//    @Autowired
//    LinkFactory linkFactory;

    @Autowired
    BlobStorageService blobStorageService;

    @Autowired
    MetadataDocumentService metadataDocumentService;
//
//    @Autowired
//    MetadataDocumentRepo metadataDocumentRepo;

    @Autowired
    XmlDocumentFactory xmlDocumentFactory;

    @Autowired
    ServiceDocLinkExtractor serviceDocLinkExtractor;

//    @Autowired
//    LinkRepo linkRepo;

//    @Autowired
//    EventFactory eventFactory;


    String xml;
    XmlDoc doc;
    LocalServiceMetadataRecord metadataDocument;

    @Override
    public EventProcessor_ProcessServiceMetadataDocumentEvent externalProcessing() throws Exception {
        String sha2 = getInitiatingEvent().getSha2();
        // long endpointJobId = getInitiatingEvent().getEndpointJobId();
        xml = blobStorageService.findXML(sha2);
        doc = xmlDocumentFactory.create(xml);
        return this;
    }


    @Override
    public EventProcessor_ProcessServiceMetadataDocumentEvent internalProcessing() throws Exception {
        String sha2 = getInitiatingEvent().getSha2();
        String harvestJobId = getInitiatingEvent().getHarvestJobId();
        String linkCheckJob = getInitiatingEvent().getLinkCheckJobId();

        metadataDocument = metadataDocumentService.findLocalProcess(linkCheckJob,sha2);
        if (!(doc instanceof XmlMetadataDocument)) {
            // this shouldn't happen
            metadataDocumentService.setState(metadataDocument , ServiceMetadataDocumentState.NOT_APPLICABLE);
            logger.debug("this shouldn't happen - not an XML Metadata records: sha2:"+sha2);
            return this;
        }
        XmlMetadataDocument xmlMetadataDocument = (XmlMetadataDocument) doc;
         if (!(doc instanceof XmlServiceRecordDoc)) {
            // ignore - not a service record
            metadataDocumentService.setState(metadataDocument , ServiceMetadataDocumentState.NOT_APPLICABLE);
            logger.debug("not a service record -ignored, fileIdentifier:"+xmlMetadataDocument.getFileIdentifier()+", type:"+xmlMetadataDocument.getMetadataDocumentType());
            return this;
        }

        XmlServiceRecordDoc xmlServiceRecordDoc = (XmlServiceRecordDoc) doc;

        String serviceType = metadataDocument.getMetadataServiceType();

        if (serviceType == null){
            metadataDocumentService.setState(metadataDocument , ServiceMetadataDocumentState.NOT_APPLICABLE);
            logger.debug("service record has no service type - ignored, fileIdentifier:"+xmlMetadataDocument.getFileIdentifier());

            return this;
        }
        if (!serviceType.equalsIgnoreCase("view")
                && !serviceType.equalsIgnoreCase("download")
                && !serviceType.equalsIgnoreCase("discovery") ){
            metadataDocumentService.setState(metadataDocument, ServiceMetadataDocumentState.NOT_APPLICABLE);
            logger.debug("service record not an appropriate type - ignored, fileIdentifier:"+xmlMetadataDocument.getFileIdentifier()+", type:"+serviceType);

            return this;
        }

        List<ServiceDocumentLink> serviceLinks = metadataDocument.getServiceDocumentLinks();
        List<OperatesOnLink> operatesOnsLinks = metadataDocument.getOperatesOnLinks();
        logger.debug("extracted "+serviceLinks.size()+" service links and "+operatesOnsLinks.size()+" operatesOn links from Service Record with fileIdentifier:"+xmlServiceRecordDoc.getFileIdentifier());

        metadataDocumentService.setState(metadataDocument , ServiceMetadataDocumentState.LINKS_EXTRACTED);

        return this;
    }


    @Override
    public List<Event> newEventProcessing() {
        List<Event> result = new ArrayList<>();

        String linkCheckJob = getInitiatingEvent().getLinkCheckJobId();

        if (metadataDocumentService.completeLinkExtract(linkCheckJob)) {
            LinksFoundInAllDocuments e = eventFactory.createLinksFoundInAllDocuments(initiatingEvent);
            result.add(e);
        }
        //
//        List<ServiceDocumentLink> serviceLinks = metadataDocument.getServiceDocumentLinks();
//        List<OperatesOnLink> operatesOnsLinks = metadataDocument.getOperatesOnLinks();
//
//        for(ServiceDocumentLink link : serviceLinks) {
//            Event e = eventFactory.createProcessServiceDocLinkEvent(link.getServiceMetadataLinkId(),linkCheckJob);
//            result.add(e);
//        }
//
//        for(OperatesOnLink link : operatesOnsLinks) {
//            Event e = eventFactory.createProcessOperatesOnLinkEvent(link.getOperatesOnLinkId(),linkCheckJob);
//            result.add(e);
//        }

        return result;
    }
}
