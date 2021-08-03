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

import net.geocat.database.harvester.entities.EndpointJob;
import net.geocat.database.harvester.entities.MetadataRecord;
import net.geocat.database.harvester.repos.EndpointJobRepo;
import net.geocat.database.harvester.repos.MetadataRecordRepo;
import net.geocat.database.linkchecker.entities.LocalDatasetMetadataRecord;
import net.geocat.database.linkchecker.entities.LocalServiceMetadataRecord;
import net.geocat.database.linkchecker.repos.LocalDatasetMetadataRecordRepo;
import net.geocat.database.linkchecker.repos.LocalServiceMetadataRecordRepo;
import net.geocat.database.linkchecker.service.MetadataDocumentFactory;
import net.geocat.eventprocessor.BaseEventProcessor;
import net.geocat.events.Event;
import net.geocat.events.EventFactory;
import net.geocat.events.findlinks.ProcessDatasetMetadataDocumentEvent;
import net.geocat.events.findlinks.ProcessServiceMetadataDocumentEvent;
import net.geocat.events.findlinks.StartProcessDocumentsEvent;
import net.geocat.service.BlobStorageService;
import net.geocat.xml.XmlDatasetMetadataDocument;
import net.geocat.xml.XmlDoc;
import net.geocat.xml.XmlDocumentFactory;
import net.geocat.xml.XmlServiceRecordDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
public class EventProcessor_StartProcessDocumentsEvent extends BaseEventProcessor<StartProcessDocumentsEvent> {

    Logger logger = LoggerFactory.getLogger(EventProcessor_StartProcessDocumentsEvent.class);

    @Autowired
    EndpointJobRepo endpointJobRepo;

    @Autowired
    MetadataRecordRepo metadataRecordRepo;

    @Autowired
    MetadataDocumentFactory metadataDocumentFactory;

    @Autowired
    EventFactory eventFactory;

    @Autowired
    XmlDocumentFactory xmlDocumentFactory;

    @Autowired
    BlobStorageService blobStorageService;

    @Autowired
    LocalDatasetMetadataRecordRepo localDatasetMetadataRecordRepo;

    @Autowired
    LocalServiceMetadataRecordRepo localServiceMetadataRecordRepo;


    List<net.geocat.database.linkchecker.entities.helper.MetadataRecord> metadataRecords;

    @Override
    public EventProcessor_StartProcessDocumentsEvent externalProcessing() {
        return this;
    }


    @Override
    public EventProcessor_StartProcessDocumentsEvent internalProcessing() throws Exception {

        List<EndpointJob> endpointJobs = endpointJobRepo.findByHarvestJobId(getInitiatingEvent().getHarvestJobId());
        List<Long> endpointIds = endpointJobs.stream().map(x -> x.getEndpointJobId()).collect(Collectors.toList());

        List<MetadataRecord> records = metadataRecordRepo.findByEndpointJobIdIn(endpointIds);

        metadataRecords = new ArrayList<>();
        String linkCheckJobId = getInitiatingEvent().getLinkCheckJobId();
        for (MetadataRecord record : records) {
            net.geocat.database.linkchecker.entities.helper.MetadataRecord metadataRecord = getDoc(record, linkCheckJobId);
            if (metadataRecord != null)
                metadataRecords.add(metadataRecord);
            else
                logger.debug("sha2=" + record.getSha2() + " - not a service or dataset record.");
        }

        return this;
    }

    public net.geocat.database.linkchecker.entities.helper.MetadataRecord getDoc(MetadataRecord record, String linkCheckJobId) throws Exception {
        String sha2 = record.getSha2();
        List<MetadataRecord> metadataRecord = metadataRecordRepo.findBySha2(sha2);
        if (metadataRecord.isEmpty())
            return null;
        String xml = blobStorageService.findXML(sha2);


        XmlDoc doc = xmlDocumentFactory.create(xml);

        if (doc instanceof XmlServiceRecordDoc) {
            XmlServiceRecordDoc xmlServiceRecordDoc = (XmlServiceRecordDoc) doc;
            LocalServiceMetadataRecord localServiceMetadataRecord =
                    metadataDocumentFactory.createLocalServiceMetadataRecord(xmlServiceRecordDoc, record.getMetadataRecordId(), linkCheckJobId, sha2);
            LocalServiceMetadataRecord existing = localServiceMetadataRecordRepo.findFirstByLinkCheckJobIdAndSha2(linkCheckJobId, sha2);
            if (existing != null)
                return existing;
            else
                return localServiceMetadataRecordRepo.save(localServiceMetadataRecord);
        } else if (doc instanceof XmlDatasetMetadataDocument) {
            XmlDatasetMetadataDocument xmlDatasetMetadataDocument = (XmlDatasetMetadataDocument) doc;
            LocalDatasetMetadataRecord localDatasetMetadataRecord =
                    metadataDocumentFactory.createLocalDatasetMetadataRecord(xmlDatasetMetadataDocument, record.getMetadataRecordId(), linkCheckJobId, sha2);

            LocalDatasetMetadataRecord existing = localDatasetMetadataRecordRepo.findFirstByLinkCheckJobIdAndSha2(linkCheckJobId, sha2);
            if (existing == null)
                return localDatasetMetadataRecordRepo.save(localDatasetMetadataRecord);
            else
                return existing;
        } else {
            return null;
        }

    }


    @Override
    public List<Event> newEventProcessing() {
        List<Event> result = new ArrayList<>();
        String linkCheckJobId = getInitiatingEvent().getLinkCheckJobId();
        String harvestJobId = getInitiatingEvent().getHarvestJobId();

        logger.debug("There are "+metadataRecords.size()+" records to process.");

        for (net.geocat.database.linkchecker.entities.helper.MetadataRecord record : metadataRecords) {
            if (record instanceof  LocalServiceMetadataRecord) {
                ProcessServiceMetadataDocumentEvent e =
                        eventFactory.createProcessServiceMetadataDocumentEvent(linkCheckJobId,
                                harvestJobId,
                                record.getSha2(),
                                record.getMetadataRecordType());
                result.add(e);
            } else if (record instanceof  LocalDatasetMetadataRecord) {
                ProcessDatasetMetadataDocumentEvent e =
                        eventFactory.createProcessDatasetMetadataDocumentEvent(linkCheckJobId,
                                harvestJobId,
                                record.getSha2(),
                                record.getMetadataRecordType());
                result.add(e);
            }
        }
        return result;
    }
}
