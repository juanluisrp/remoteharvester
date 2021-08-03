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


import net.geocat.database.linkchecker.repos.LinkCheckJobRepo;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.PrintWriter;
import java.io.StringWriter;

@Component
@Scope("prototype")
public class DatabaseUpdateService {

    @Autowired
    LinkCheckJobRepo linkCheckJobRepo;
//
//    @Autowired
//    public EndpointJobService endpointJobService;
//    @Autowired
//    public EventFactory eventFactory;
//    @Autowired
//    private EndpointJobRepo endpointJobRepo;
//    @Autowired
//    private HarvestJobRepo harvestJobRepo;

//    public Object updateDatabase(Object obj) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        Method m  = getClass().getMethod("updateDatabase", obj.getClass());
//        return m.invoke(obj);
//    }

    //idempotent transaction
//    public List<CSWEndPointDetectedEvent> updateDatabase(CSWMetadata cswMetadata) {
//        EndpointJob endpointJob = endpointJobRepo.findById(cswMetadata.getEndpointId()).get();
//        endpointJob.setExpectedNumberOfRecords(cswMetadata.getNumberOfExpectedRecords());
//        endpointJob.setUrlGetRecords(cswMetadata.getGetRecordsUrl());
//
//        List<CSWEndPointDetectedEvent> result = createCSWEndPointDetectedEvents(cswMetadata);
//        // endpointJob.setState(EndpointJobState.WORK_DETERMINED);
//        endpointJobRepo.save(endpointJob);
//        return result;
//    }
//
//    public List<CSWEndPointDetectedEvent> createCSWEndPointDetectedEvents(CSWMetadata metadata) {
//        List<CSWEndPointDetectedEvent> result = new ArrayList<>();
//        for (List<String> urlSet : metadata.getNestedGetCapUrls()) {
//            boolean noActionRequired = endpointJobService.areTheseUrlsInDB(metadata.getHarvesterId(), urlSet);
//            if (!noActionRequired) {
//                String url = urlSet.get(0);
//                EndpointJob job = endpointJobService.createInitial(metadata.getHarvesterId(), url, metadata.getFilter(), metadata.isLookForNestedDiscoveryService());
//                result.add(eventFactory.create_CSWEndPointDetectedEvent(job.getHarvestJobId(), job.getEndpointJobId(), url, metadata.getFilter(), metadata.isLookForNestedDiscoveryService()));
//            }
//        }
//        return result;
//    }

    public static String convertToString(Throwable e) {
        String result = e.getClass().getCanonicalName() + " - " + e.getMessage();

        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String stackTraceStr = sw.toString();

        result += stackTraceStr;
        if (e.getCause() != null)
            return result + convertToString(e.getCause());
        return result;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    //synchronized so other threads cannot update while we are writing...
    public synchronized void errorOccurred(Exchange exchange) {
//        Exception e = (Exception) exchange.getMessage().getHeader("exception");
//        if (e == null)
//            return;
//        String processId = (String) exchange.getMessage().getHeader("processID");
//        Optional<LinkCheckJob> _job = linkCheckJobRepo.findById(processId);
//        if (!_job.isPresent())
//            return; // cannot update database.  Likely DB issue or very very early exception
//        LinkCheckJob job = _job.get();
//        if (job.getMessages() == null)
//            job.setMessages("");
//        String thisMessage = "\n--------------------------------------\n";
//        thisMessage += "WHEN:" + Instant.now().toString() + "\n\n";
//        thisMessage += convertToString(e);
//        thisMessage += "\n--------------------------------------\n";
//        job.setMessages(job.getMessages() + thisMessage);
//        LinkCheckJob j2 = linkCheckJobRepo.save(job);
    }

}
