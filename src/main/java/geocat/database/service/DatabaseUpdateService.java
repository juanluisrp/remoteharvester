package geocat.database.service;

import geocat.csw.CSWMetadata;
import geocat.database.entities.EndpointJob;
import geocat.database.entities.HarvestJob;
import geocat.database.repos.EndpointJobRepo;
import geocat.database.repos.HarvestJobRepo;
import geocat.events.EventService;
import geocat.events.determinework.CSWEndPointDetectedEvent;
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

@Component
@Scope("prototype")
public class DatabaseUpdateService {

    @Autowired
    public EndpointJobService endpointJobService;
    @Autowired
    public EventService eventService;
    @Autowired
    private EndpointJobRepo endpointJobRepo;
    @Autowired
    private HarvestJobRepo harvestJobRepo;

//    public Object updateDatabase(Object obj) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        Method m  = getClass().getMethod("updateDatabase", obj.getClass());
//        return m.invoke(obj);
//    }

    public List<CSWEndPointDetectedEvent> updateDatabase(CSWMetadata cswMetadata) {
        EndpointJob endpointJob = endpointJobRepo.findById(cswMetadata.getEndpointId()).get();
        endpointJob.setExpectedNumberOfRecords(cswMetadata.getNumberOfExpectedRecords());
        endpointJob.setUrlGetRecords(cswMetadata.getGetRecordsUrl());

        List<CSWEndPointDetectedEvent> result = createCSWEndPointDetectedEvents(cswMetadata);
        endpointJob.setState("WORKDETERMINED");
        endpointJobRepo.save(endpointJob);
        return result;
    }

    public List<CSWEndPointDetectedEvent> createCSWEndPointDetectedEvents(CSWMetadata metadata) {
        List<CSWEndPointDetectedEvent> result = new ArrayList<>();
        for (List<String> urlSet : metadata.getNestedGetCapUrls()) {
            boolean noActionRequired = endpointJobService.areTheseUrlsInDB(metadata.getHarvesterId(), urlSet);
            if (!noActionRequired) {
                String url = urlSet.get(0);
                EndpointJob job = endpointJobService.createInitial(metadata.getHarvesterId(), url, metadata.getFilter(), metadata.isLookForNestedDiscoveryService());
                result.add(eventService.createCSWEndPointDetectedEvent(job.getHarvestJobId(), job.getEndpointJobId(), url, metadata.getFilter(), metadata.isLookForNestedDiscoveryService()));
            }
        }
        return result;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    //synchronized so other threads cannot update while we are writing...
    public synchronized void errorOccurred(Exchange exchange) {
        Exception e = (Exception) exchange.getMessage().getHeader("exception");
        if (e == null)
            return;
        String processId = (String) exchange.getMessage().getHeader("processID");
        HarvestJob job = harvestJobRepo.findById(processId).get();
        if (job.getMessages() == null)
            job.setMessages("");
        String thisMessage = "\n--------------------------------------\n";
        thisMessage += "WHEN:" + Instant.now().toString() + "\n\n";
        thisMessage += convertToString(e);
        thisMessage += "\n--------------------------------------\n";
        job.setMessages(job.getMessages() + thisMessage);
        HarvestJob j2 = harvestJobRepo.save(job);
    }


    public String convertToString(Throwable e) {
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
