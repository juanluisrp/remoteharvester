package geocat.eventprocessor.processors.harvest;

import geocat.csw.CSWService;
import geocat.csw.csw.CSWGetRecordsHandler;
import geocat.database.entities.EndpointJobState;
import geocat.database.entities.RecordSet;
import geocat.database.service.EndpointJobService;
import geocat.database.service.RecordSetService;
import geocat.eventprocessor.BaseEventProcessor;
import geocat.eventprocessor.processors.GetRecordsResult;
import geocat.events.Event;
import geocat.events.actualRecordCollection.EndpointHarvestComplete;
import geocat.events.actualRecordCollection.GetRecordsCommand;
import geocat.service.MetadataExploderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class EventProcessor_GetRecordsCommand extends BaseEventProcessor<GetRecordsCommand> {

    @Autowired
    CSWService cswService;

    @Autowired
    CSWGetRecordsHandler cswGetRecordsHandler;

    @Autowired
    RecordSetService recordSetService;

    @Autowired
    EndpointJobService endpointJobService;

    @Autowired
    MetadataExploderService metadataExploderService;

    GetRecordsResult result;

    public EventProcessor_GetRecordsCommand() {
        super();
    }

    @Override
    public EventProcessor_GetRecordsCommand internalProcessing() throws Exception {
        GetRecordsCommand cmd = getInitiatingEvent();
        recordSetService.update(cmd.getRecordSetId(),  result.getNumberRecordsReturned());
        RecordSet recordSet = recordSetService.getById(cmd.getRecordSetId());
        metadataExploderService.explode(recordSet,result.getXmlGetRecordsResult());
        return this;
    }

    @Override
    public EventProcessor_GetRecordsCommand externalProcessing() throws Exception {

        GetRecordsCommand e = getInitiatingEvent();
        String xml = cswService.GetRecords(e.getGetRecordsURL(), e.getFilter(), e.getStartRecordNumber(), e.getEndRecordNumber());
        Document xmlParsed = MetadataExploderService.parseXML(xml);
        int nrecords = cswGetRecordsHandler.extractActualNumberOfRecordsReturned(xmlParsed);

        int nextRecordNumber = cswGetRecordsHandler.extractNextRecordNumber(xml); // we could test to see if this is 0 if this is the lastone (but this brittle)
        if (nrecords != e.expectedNumberOfRecords())
            throw new Exception("got " + nrecords + ", but expected " + e.expectedNumberOfRecords()); // TODO: might not want to throw
        result = new GetRecordsResult(xml, nrecords, xmlParsed);
        return this;
    }

    @Override
    public List<Event> newEventProcessing() {
        List<Event> result = new ArrayList<>();
        if (recordSetService.complete(getInitiatingEvent().getEndPointId())) {
            EndpointHarvestComplete e = new EndpointHarvestComplete(getInitiatingEvent().getEndPointId(),
                    getInitiatingEvent().getHarvesterId());
            endpointJobService.updateState(getInitiatingEvent().getEndPointId(), EndpointJobState.RECORDS_RECEIVED);
            result.add(e);
        }
        return result;
    }
}
