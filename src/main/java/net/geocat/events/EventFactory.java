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

package net.geocat.events;

import net.geocat.database.linkchecker.entities2.Link;
import net.geocat.events.findlinks.LinksFoundInAllDocuments;
import net.geocat.events.findlinks.ProcessDatasetMetadataDocumentEvent;
import net.geocat.events.findlinks.ProcessServiceMetadataDocumentEvent;
import net.geocat.events.findlinks.StartProcessDocumentsEvent;
import net.geocat.events.processlinks.AllLinksCheckedEvent;
import net.geocat.events.processlinks.ProcessOperatesOnLinkEvent;
import net.geocat.events.processlinks.ProcessServiceDocLinkEvent;
import net.geocat.xml.MetadataDocumentType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class EventFactory {


    public StartProcessDocumentsEvent createStartProcessDocumentsEvent(LinkCheckRequestedEvent linkCheckRequestedEvent) {
        StartProcessDocumentsEvent result = new StartProcessDocumentsEvent(
                linkCheckRequestedEvent.getLinkCheckJobId(), linkCheckRequestedEvent.getHarvestJobId()
        );
        return result;
    }

    public ProcessServiceMetadataDocumentEvent createProcessServiceMetadataDocumentEvent(String linkCheckJobId,
                                                                                  String harvestJobId,
                                                                                  //  long endpointJobId,
                                                                                  String sha2,
                                                                                  MetadataDocumentType documentType) {
        ProcessServiceMetadataDocumentEvent result = new ProcessServiceMetadataDocumentEvent(linkCheckJobId, harvestJobId, sha2, documentType);
        return result;
    }

//    public MetadataDocumentProcessedEvent createMetadataDocumentProcessedEvent(ProcessMetadataDocumentEvent e  ) {
//        MetadataDocumentProcessedEvent result = new MetadataDocumentProcessedEvent(
//                e.getLinkCheckJobId(), e.getHarvestJobId(), e.getEndpointJobId(), e.getSha2() );
//        return result;
//    }

    public LinksFoundInAllDocuments createLinksFoundInAllDocuments(ProcessServiceMetadataDocumentEvent initiatingEvent) {
        LinksFoundInAllDocuments result = new LinksFoundInAllDocuments(initiatingEvent.getLinkCheckJobId(), initiatingEvent.getHarvestJobId());
        return result;
    }

    public ProcessServiceDocLinkEvent createProcessLinkEvent(Link l) {
        ProcessServiceDocLinkEvent result = new ProcessServiceDocLinkEvent(l.getLinkId(), l.getLinkCheckJobId());
        return result;
    }

    public AllLinksCheckedEvent createAllLinksCheckedEvent(String linkcheckJobId) {
        AllLinksCheckedEvent event = new AllLinksCheckedEvent(linkcheckJobId);
        return event;
    }

    public ProcessDatasetMetadataDocumentEvent createProcessDatasetMetadataDocumentEvent(String linkCheckJobId, String harvestJobId, String sha2, MetadataDocumentType metadataRecordType) {
        ProcessDatasetMetadataDocumentEvent result = new ProcessDatasetMetadataDocumentEvent(linkCheckJobId, harvestJobId, sha2, metadataRecordType);
        return result;
    }

    public ProcessServiceDocLinkEvent createProcessServiceDocLinkEvent(long linkId, String linkCheckJobId){
        ProcessServiceDocLinkEvent result = new ProcessServiceDocLinkEvent(linkId,linkCheckJobId);
        return result;
    }

    public ProcessOperatesOnLinkEvent createProcessOperatesOnLinkEvent(long linkId, String linkCheckJobId){
        ProcessOperatesOnLinkEvent result = new ProcessOperatesOnLinkEvent(linkId,linkCheckJobId);
        return result;
    }

}
