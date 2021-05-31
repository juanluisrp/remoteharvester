package geocat.eventprocessor.processors.harvest;

import geocat.database.entities.RecordSet;
import geocat.database.service.RecordSetService;
import geocat.eventprocessor.BaseEventProcessor;
import geocat.events.Event;
import geocat.events.EventFactory;
import geocat.events.actualRecordCollection.ActualHarvestEndpointStartCommand;
import geocat.events.actualRecordCollection.GetRecordsCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class EventProcessor_ActualHarvestEndpointStartCommand extends BaseEventProcessor<ActualHarvestEndpointStartCommand>
{

    @Autowired
    RecordSetService recordSetService;

    @Autowired
    EventFactory eventFactory;

    public EventProcessor_ActualHarvestEndpointStartCommand(){
        super();
    }

    @Override
    public EventProcessor_ActualHarvestEndpointStartCommand  internalProcessing() throws Exception {
        ActualHarvestEndpointStartCommand cmd = getInitiatingEvent();
        if (cmd.getExpectedNumberOfRecords() <= 0)
            throw new Exception("getExpectedNumberOfRecords <= 0");
        if (cmd.getnRecordPerRequest() <= 0)
            throw new Exception("getnRecordPerRequest <= 0");

        List<RecordSet> records = new ArrayList<>();
        //for example, for getting 10 records;
        //  first one - 1 to 10 (start at 1, get 10)
        //  2nd       - 11 to 20 (start at 11, get 10)
        for(int idx=1; idx<= cmd.getExpectedNumberOfRecords(); idx += cmd.getnRecordPerRequest()) {
            int start = idx;
            int end = idx + cmd.getnRecordPerRequest()-1;
            if (end >cmd.getExpectedNumberOfRecords())
                end = cmd.getExpectedNumberOfRecords();
            boolean lastOne = (end==cmd.getExpectedNumberOfRecords());
            RecordSet recordSet = recordSetService.create(start,end, cmd, lastOne);
            records.add(recordSet);
        }
        return this;
    }

    @Override
    public EventProcessor_ActualHarvestEndpointStartCommand externalProcessing(){
        return this;
    }

    @Override
    public List<Event> newEventProcessing(){
        List<Event> result = new ArrayList<>();
        String endpointId = getInitiatingEvent().getEndPointId();
        List<RecordSet> records =  recordSetService.getAll(endpointId);;
        for(RecordSet record : records){
            GetRecordsCommand command = eventFactory.create_GetRecordsCommand(getInitiatingEvent(),
                    record.getStartRecordNumber(),
                    record.getEndRecordNumber(),
                    record.getRecordSetId());
            result.add(command);
        }
        return result;
    }
}