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

package net.geocat.service;

import net.geocat.database.linkchecker.entities.LinkCheckJob;
import net.geocat.database.linkchecker.entities.helper.StatusQueryItem;
import net.geocat.database.linkchecker.repos.LinkCheckJobRepo;
import net.geocat.database.linkchecker.repos.LocalDatasetMetadataRecordRepo;
import net.geocat.database.linkchecker.repos.LocalNotProcessedMetadataRecordRepo;
import net.geocat.database.linkchecker.repos.LocalServiceMetadataRecordRepo;
import net.geocat.model.DocumentTypeStatus;
import net.geocat.model.LinkCheckStatus;
import net.geocat.model.StatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class GetStatusService {

    @Autowired
    LocalServiceMetadataRecordRepo localServiceMetadataRecordRepo;

    @Autowired
    LocalDatasetMetadataRecordRepo localDatasetMetadataRecordRepo;

    @Autowired
    LocalNotProcessedMetadataRecordRepo localNotProcessedMetadataRecordRepo;

    @Autowired
    LinkCheckJobRepo linkCheckJobRepo;

    public LinkCheckStatus getStatus(String processID) {
        LinkCheckJob job = linkCheckJobRepo.findById(processID).get();

        LinkCheckStatus result = new LinkCheckStatus(processID, job.getState());
        result.setServiceRecordStatus( computeServiceRecords(processID));
        result.setDatasetRecordStatus( computeDatasetRecords(processID));

        return result;
    }

    public DocumentTypeStatus computeServiceRecords(String processID){
        long nrecords = localServiceMetadataRecordRepo.countByLinkCheckJobId(processID);
        List<StatusQueryItem> statusList = localServiceMetadataRecordRepo.getStatus(processID);
        List<StatusType> statusTypeList = new ArrayList<>();
        for(StatusQueryItem item: statusList){
            StatusType statusType = new StatusType(item.getState().toString(),item.getNumberOfRecords());
            statusTypeList.add(statusType);
        }

        DocumentTypeStatus result = new DocumentTypeStatus("ServiceRecord",nrecords,statusTypeList);
        return result;
    }

    public DocumentTypeStatus computeDatasetRecords(String processID){
        long nrecords = localDatasetMetadataRecordRepo.countByLinkCheckJobId(processID);
        List<StatusQueryItem> statusList = localDatasetMetadataRecordRepo.getStatus(processID);
        List<StatusType> statusTypeList = new ArrayList<>();
        for(StatusQueryItem item: statusList){
            StatusType statusType = new StatusType(item.getState().toString(),item.getNumberOfRecords());
            statusTypeList.add(statusType);
        }

        DocumentTypeStatus result = new DocumentTypeStatus("DatasetRecord",nrecords,statusTypeList);
        return result;
    }

}
