package geocat.events;

import geocat.database.entities.EndpointJob;
import geocat.database.entities.HarvestJob;
import geocat.events.actualRecordCollection.ActualHarvestEndpointStartCommand;
import geocat.events.actualRecordCollection.GetRecordsCommand;
import geocat.events.determinework.CSWEndPointDetectedEvent;
import geocat.events.determinework.CSWEndpointWorkDetermined;
import geocat.events.determinework.DetermineWorkStartCommand;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class EventFactory {

    public ActualHarvestEndpointStartCommand create_ActualHarvestEndpointStartCommand(EndpointJob job, HarvestJob harvestJob) {
        ActualHarvestEndpointStartCommand result = new ActualHarvestEndpointStartCommand();
        result.setFilter(harvestJob.getFilter());
        result.setHarvesterId(harvestJob.getJobId());
        result.setEndPointId(job.getEndpointJobId());
        result.setGetRecordsURL(job.getUrlGetRecords());
        result.setExpectedNumberOfRecords(job.getExpectedNumberOfRecords());
        result.setMaxSimultaneousRequests(1);
        result.setnRecordPerRequest(harvestJob.getNrecordsPerRequest());
        return result;
    }

    public GetRecordsCommand create_GetRecordsCommand(ActualHarvestEndpointStartCommand cmd, int startRecord, int endRecord, String recordSetId, boolean lastOne) {
        GetRecordsCommand result = new GetRecordsCommand();

        result.setEndPointId(cmd.getEndPointId());
        result.setHarvesterId(cmd.getHarvesterId());
        result.setFilter(cmd.getFilter());
        result.setGetRecordsURL(cmd.getGetRecordsURL());
        result.setStartRecordNumber(startRecord);
        result.setEndRecordNumber(endRecord);
        result.setRecordSetId(recordSetId);
        result.setLastSet(lastOne);
        result.setTotalRecordsInQuery(cmd.getExpectedNumberOfRecords());

        return result;
    }

    public CSWEndPointDetectedEvent create_CSWEndPointDetectedEvent(String harvestId,
                                                                    String endpointId,
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

    public CSWEndpointWorkDetermined create_CSWEndpointWorkDetermined(String harvestId, String endpointId) {
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

}
