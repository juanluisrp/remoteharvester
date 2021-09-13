package geocat.events;

import geocat.database.entities.EndpointJob;
import geocat.database.entities.HarvestJob;
import geocat.events.actualRecordCollection.ActualHarvestEndpointStartCommand;
import geocat.events.actualRecordCollection.GetRecordsCommand;
import geocat.events.determinework.CSWEndPointDetectedEvent;
import geocat.events.determinework.CSWEndpointWorkDetermined;
import geocat.events.determinework.DetermineWorkStartCommand;
import geocat.service.QueueChooserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class EventFactory {

    @Autowired
    QueueChooserService queueChooserService;

    public ActualHarvestEndpointStartCommand create_ActualHarvestEndpointStartCommand(EndpointJob job, HarvestJob harvestJob) throws Exception {
        ActualHarvestEndpointStartCommand result = new ActualHarvestEndpointStartCommand();
        result.setDoNotSort(harvestJob.getDoNotSort());
        result.setFilter(harvestJob.getFilter());
        result.setHarvesterId(harvestJob.getJobId());
        result.setEndPointId(job.getEndpointJobId());
        result.setGetRecordsURL(job.getUrlGetRecords());
        result.setExpectedNumberOfRecords(job.getExpectedNumberOfRecords());
        result.setnRecordPerRequest(harvestJob.getNrecordsPerRequest());
        result.setRecordQueueHint(harvestJob.getGetRecordQueueHint());
        result.setActualGetRecordQueue(queueChooserService.chooseQueue(harvestJob.getGetRecordQueueHint(), job.getExpectedNumberOfRecords()));
        return result;
    }

    public GetRecordsCommand create_GetRecordsCommand(ActualHarvestEndpointStartCommand cmd, int startRecord, int endRecord, long recordSetId, boolean lastOne) {
        GetRecordsCommand result = new GetRecordsCommand();

        result.setDoNotSort(cmd.getDoNotSort());
        result.setEndPointId(cmd.getEndPointId());
        result.setHarvesterId(cmd.getHarvesterId());
        result.setFilter(cmd.getFilter());
        result.setGetRecordsURL(cmd.getGetRecordsURL());
        result.setStartRecordNumber(startRecord);
        result.setEndRecordNumber(endRecord);
        result.setRecordSetId(recordSetId);
        result.setLastSet(lastOne);
        result.setTotalRecordsInQuery(cmd.getExpectedNumberOfRecords());
        result.setWorkQueueName(cmd.getActualGetRecordQueue());

        return result;
    }

    public CSWEndPointDetectedEvent create_CSWEndPointDetectedEvent(String harvestId,
                                                                    long endpointId,
                                                                    String url,
                                                                    String filter,
                                                                    boolean lookForNestedDiscoveryService) {
        CSWEndPointDetectedEvent result = new CSWEndPointDetectedEvent();
        result.setHarvesterId(harvestId);
        result.setEndPointId(endpointId);
        result.setUrl(url);
        result.setFilter(filter);
        result.setLookForNestedDiscoveryService(lookForNestedDiscoveryService);
        return result;
    }

    public CSWEndpointWorkDetermined create_CSWEndpointWorkDetermined(String harvestId, long endpointId) {
        return new CSWEndpointWorkDetermined(harvestId, endpointId);
    }

    public DetermineWorkStartCommand create_StartWorkCommand(HarvestJob job) {
        return new DetermineWorkStartCommand(
                job.getJobId(),
                job.getInitialUrl(),
                job.getFilter(),
                job.isLookForNestedDiscoveryService()
        );
    }

    public HarvestAbortEvent create_HarvestAbortEvent(String processID) {
        return new HarvestAbortEvent(processID);
    }

}
