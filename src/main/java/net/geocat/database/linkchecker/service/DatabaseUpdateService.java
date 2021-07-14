package net.geocat.database.linkchecker.service;


import net.geocat.database.linkchecker.entities.LinkCheckJob;
import net.geocat.database.linkchecker.repos.LinkCheckJobRepo;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    //synchronized so other threads cannot update while we are writing...
    public synchronized void errorOccurred(Exchange exchange) {
        Exception e = (Exception) exchange.getMessage().getHeader("exception");
        if (e == null)
            return;
        String processId = (String) exchange.getMessage().getHeader("processID");
        Optional<LinkCheckJob> _job = linkCheckJobRepo.findById(processId);
        if (!_job.isPresent())
            return; // cannot update database.  Likely DB issue or very very early exception
        LinkCheckJob job = _job.get();
        if (job.getMessages() == null)
            job.setMessages("");
        String thisMessage = "\n--------------------------------------\n";
        thisMessage += "WHEN:" + Instant.now().toString() + "\n\n";
        thisMessage += convertToString(e);
        thisMessage += "\n--------------------------------------\n";
        job.setMessages(job.getMessages() + thisMessage);
        LinkCheckJob j2 = linkCheckJobRepo.save(job);
    }



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

}
