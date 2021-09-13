package geocat.events;

import geocat.database.service.EndpointJobService;
import geocat.model.HarvesterConfig;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope("prototype")
public class EventService {

    @Autowired
    public EndpointJobService endpointJobService;

//    public DetermineWorkStartCommand createStartWorkCommand(HarvestJob job) {
//        return new DetermineWorkStartCommand(
//                job.getJobId(),
//                job.getInitialUrl(),
//                job.getFilter(),
//                job.isLookForNestedDiscoveryService()
//        );
//    }


    //calls validate on the (parsed) input message
    public void validateHarvesterConfig(Message message) throws Exception {
        ((HarvesterConfig) message.getBody()).validate();
    }

    //creates a new GUID
    public String createGUID() {
        UUID guid = java.util.UUID.randomUUID();
        return guid.toString();
    }

    /**
     * remove all headers from the request
     * add processID=GUID  to be used for this harvest
     */
    public void addGUID(Message message) {
        message.getHeaders().clear();
        String guid = createGUID();
        message.getHeaders().put("processID", guid);
        message.getHeaders().put("JMSCorrelationID", guid);
        ((HarvesterConfig) message.getBody()).setProcessID(guid);

    }


    //we are doing trivial JSON conversion
    //   take the processID from the header, and return it as a json string like;
    //{
    //     "processID":"5fcd5f22-1a40-4712-8d2d-ca88c2d0d472"
    //}
    public void resultJSON(Message message) {
        String uuid = ((HarvesterConfig) message.getBody()).getProcessID();
        message.setBody("{\n     \"processID\":\"" + uuid + "\"\n}\n");
    }

    public HarvestRequestedEvent createHarvestRequestedEvent(HarvesterConfig harvesterConfig, String processID) {
        HarvestRequestedEvent result = new HarvestRequestedEvent();
        result.setDoNotSort(harvesterConfig.getDoNotSort());
        result.setHarvestId(processID);
        result.setUrl(harvesterConfig.getUrl());
        result.setFilter(harvesterConfig.getFilter());
        result.setLookForNestedDiscoveryService(harvesterConfig.isLookForNestedDiscoveryService());
        result.setLongTermTag(harvesterConfig.getLongTermTag());
        result.setProblematicResultsConfigurationJSON(harvesterConfig.getProblematicResultsConfiguration().toString());
        result.setNumberRecordsPerRequest(harvesterConfig.getNumberOfRecordsPerRequest());
        result.setGetRecordQueueHint(harvesterConfig.getGetRecordQueueHint());
        return result;
    }


//    public CSWEndPointDetectedEvent createCSWEndPointDetectedEvent(String harvestId,
//                                                                   String endpointId,
//                                                                   String url,
//                                                                   String filter,
//                                                                   boolean lookForNestedDiscoveryService) {
//        CSWEndPointDetectedEvent result = new CSWEndPointDetectedEvent();
//        result.setHarvesterId(harvestId);
//        result.setEndPointId(endpointId);
//        result.setUrl(url);
//        result.setFilter(filter);
//        result.setLookForNestedDiscoveryService(lookForNestedDiscoveryService);
//        return result;
//    }

//    public CSWEndpointWorkDetermined createCSWEndpointWorkDetermined(String harvestId, String endpointId) {
//        return new CSWEndpointWorkDetermined(harvestId, endpointId);
//    }


//    public GetRecordsCommand createGetRecordsCommand(ActualHarvestEndpointStartCommand info, int startRecord, int endRecord, String recordSetId) {
//        GetRecordsCommand result = new GetRecordsCommand();
//        result.setEndPointId(info.getEndPointId());
//        result.setHarvesterId(info.getHarvesterId());
//        result.setFilter(info.getFilter());
//        result.setGetRecordsURL(info.getGetRecordsURL());
//        result.setStartRecordNumber(startRecord);
//        result.setEndRecordNumber(endRecord);
//        result.setRecordSetId(recordSetId);
//        return result;
//    }

}
