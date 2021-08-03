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

package net.geocat.database.linkchecker.service;

import net.geocat.database.linkchecker.entities.LocalDatasetMetadataRecord;
import net.geocat.database.linkchecker.entities.LocalServiceMetadataRecord;
import net.geocat.database.linkchecker.entities.helper.ServiceMetadataDocumentState;
import net.geocat.database.linkchecker.entities2.MetadataDocument;
import net.geocat.database.linkchecker.entities2.MetadataDocumentState;
import net.geocat.database.linkchecker.repos.LocalDatasetMetadataRecordRepo;
import net.geocat.database.linkchecker.repos.LocalServiceMetadataRecordRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class MetadataDocumentService {

    @Autowired
    LocalServiceMetadataRecordRepo localServiceMetadataRecordRepo;

    @Autowired
    LocalDatasetMetadataRecordRepo localDatasetMetadataRecordRepo;

//    @Autowired
//    public MetadataDocumentRepo metadataDocumentRepo;

    public MetadataDocument create(String linkCheckJobId, String sha2, Long harvesterMetadataRecordId) {
//        MetadataDocument doc = metadataDocumentRepo.findFirstByLinkCheckJobIdAndSha2(linkCheckJobId,sha2);
//        if (doc != null)
//            return doc;
//        doc = new MetadataDocument();
//        doc.setLinkCheckJobId(linkCheckJobId);
//        doc.setSha2(sha2);
//        doc.setHarvesterMetadataRecordId(harvesterMetadataRecordId);
//        doc.setState(MetadataDocumentState.IN_PROGRESS);
//        return metadataDocumentRepo.save(doc);
        return null;
    }

    public LocalDatasetMetadataRecord findLocalDataset(String linkCheckJobId, String sha2) {
        LocalDatasetMetadataRecord doc = localDatasetMetadataRecordRepo.findFirstByLinkCheckJobIdAndSha2(linkCheckJobId,sha2);
        return doc;
    }

    public LocalServiceMetadataRecord findLocalProcess(String linkCheckJobId, String sha2) {
        LocalServiceMetadataRecord doc = localServiceMetadataRecordRepo.findFirstByLinkCheckJobIdAndSha2(linkCheckJobId,sha2);
        return doc;
    }

//    public MetadataDocument setState(long metadataDocumentId, MetadataDocumentState state){
//        MetadataDocument doc = metadataDocumentRepo.findById(metadataDocumentId).get();
//        doc.setState(state);
//        return metadataDocumentRepo.save(doc);
//    }

    public LocalServiceMetadataRecord setState(LocalServiceMetadataRecord localServiceMetadataRecord, ServiceMetadataDocumentState state) {
        LocalServiceMetadataRecord doc = localServiceMetadataRecordRepo.findById(localServiceMetadataRecord.getServiceMetadataDocumentId()).get();
        doc.setState(state);
        return localServiceMetadataRecordRepo.save(doc);
    }

    public LocalDatasetMetadataRecord setState(LocalDatasetMetadataRecord localServiceMetadataRecord, ServiceMetadataDocumentState state) {
        LocalDatasetMetadataRecord doc = localDatasetMetadataRecordRepo.findById(localServiceMetadataRecord.getDatasetMetadataDocumentId()).get();
        doc.setState(state);
        return localDatasetMetadataRecordRepo.save(doc);
    }

    public boolean completeLinkExtract(String linkCheckJobId) {
        long nrecordsService = localServiceMetadataRecordRepo.countByLinkCheckJobId(linkCheckJobId);
        long nrecordsServiceComplete = localServiceMetadataRecordRepo.countCompletedState(linkCheckJobId);

        long nrecordsDataset = localDatasetMetadataRecordRepo.countByLinkCheckJobId(linkCheckJobId);
        long nrecordsDatasetComplete = localDatasetMetadataRecordRepo.countCompletedState(linkCheckJobId);

        return (nrecordsService+nrecordsDataset ) == (nrecordsServiceComplete+nrecordsDatasetComplete);
     }
}
