package geocat.database.service;

import geocat.database.entities.RecordSet;
import geocat.database.repos.RecordSetRepo;
import geocat.events.actualRecordCollection.ActualHarvestEndpointStartCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Scope("prototype")
public class RecordSetService {

    @Autowired
    RecordSetRepo recordSetRepo;

    public RecordSet create(int start, int end, ActualHarvestEndpointStartCommand cmd, boolean lastSet) {
        RecordSet record = recordSetRepo.findByHarvestJobIdAndEndpointJobIdAndStartRecordNumber(cmd.getHarvesterId(),
                cmd.getEndPointId(),
                start
        );
        if (record != null)
            return record;

        RecordSet result = new RecordSet();
        result.setRecordSetId(UUID.randomUUID().toString());
        result.setStartRecordNumber(start);
        result.setEndRecordNumber(end);
        result.setExpectedNumberRecords(end - start + 1);
        result.setEndpointJobId(cmd.getEndPointId());
        result.setHarvestJobId(cmd.getHarvesterId());
        result.setLastSet(lastSet);

        return recordSetRepo.save(result);
    }

    public RecordSet update(String recordSetId,  int numberRecordsReturned) {
        RecordSet record = recordSetRepo.findById(recordSetId).get();
      //  record.setGetRecordResponse(xmlGetRecordsResult);
        record.setActualNumberRecords(numberRecordsReturned);
        return recordSetRepo.save(record);
    }

    public boolean complete(String endpointId) {
        List<RecordSet> records = recordSetRepo.findByEndpointJobId(endpointId);
        boolean allDone = true;
        for (RecordSet recordSet : records) {
            boolean thisRecordDone = (recordSet.getActualNumberRecords() != null) ;
            allDone = allDone && thisRecordDone;
        }
        return allDone;
    }

    public RecordSet getById(String recordSetId) {
        return recordSetRepo.findById(recordSetId).get();
    }


    public List<RecordSet> getAll(String endpointId) {
        return recordSetRepo.findByEndpointJobId(endpointId);
    }
}
