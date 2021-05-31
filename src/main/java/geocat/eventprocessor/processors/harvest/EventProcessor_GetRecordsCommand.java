package geocat.eventprocessor.processors.harvest;

import geocat.csw.CSWService;
import geocat.csw.csw.CSWGetRecordsHandler;
import geocat.database.service.EndpointJobService;
import geocat.database.service.RecordSetService;
import geocat.eventprocessor.processors.GetRecordsResult;
import geocat.eventprocessor.BaseEventProcessor;
import geocat.events.Event;
import geocat.events.actualRecordCollection.EndpointHarvestComplete;
import geocat.events.actualRecordCollection.GetRecordsCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class EventProcessor_GetRecordsCommand extends BaseEventProcessor<GetRecordsCommand>
{

    @Autowired
    CSWService cswService;

    @Autowired
    CSWGetRecordsHandler cswGetRecordsHandler;

    @Autowired
    RecordSetService recordSetService;

    @Autowired
    EndpointJobService endpointJobService;

    GetRecordsResult result;

    public EventProcessor_GetRecordsCommand(){
        super();
    }

    @Override
    public EventProcessor_GetRecordsCommand  internalProcessing(){
        GetRecordsCommand cmd = getInitiatingEvent();
        recordSetService.update(cmd.getRecordSetId(), result.getXmlGetRecordsResult(),result.getNumberRecordsReturned());
        return this;
    }

    @Override
    public EventProcessor_GetRecordsCommand externalProcessing() throws Exception {

        GetRecordsCommand e = getInitiatingEvent();
        String xml = cswService.GetRecords(e.getGetRecordsURL(),e.getFilter(),e.getStartRecordNumber(),e.getEndRecordNumber());
        int nrecords = cswGetRecordsHandler.extractActualNumberOfRecordsReturned(xml);
        // int nextRecordNumber = cswGetRecordsHandler.extractNextRecordNumber(xml); // we could test to see if this is 0 if this is the lastone (but this brittle)
        if (nrecords != e.expectedNumberOfRecords())
            throw new Exception("got "+nrecords+", but expected "+e.expectedNumberOfRecords()); // TODO: might not want to throw
        result = new GetRecordsResult(xml,nrecords);
        return this;
    }

    @Override
    public List<Event> newEventProcessing(){
        List<Event> result  = new ArrayList<>();
        if (recordSetService.complete(getInitiatingEvent().getEndPointId()))
        {
            EndpointHarvestComplete e = new EndpointHarvestComplete(getInitiatingEvent().getEndPointId(),
                    getInitiatingEvent().getHarvesterId());
            endpointJobService.updateState(getInitiatingEvent().getEndPointId(),"HARVESTFINISHED");
            result.add(e);
        }
        return result;
    }
}
