package geocat.eventprocessor.processors.harvest;

import geocat.csw.CSWService;
import geocat.csw.csw.CSWGetRecordsHandler;
import geocat.database.entities.EndpointJob;
import geocat.database.entities.EndpointJobState;
import geocat.database.entities.HarvestJob;
import geocat.database.entities.RecordSet;
import geocat.database.service.EndpointJobService;
import geocat.database.service.HarvestJobService;
import geocat.database.service.RecordSetService;
import geocat.eventprocessor.BaseEventProcessor;
import geocat.eventprocessor.MainLoopRouteCreator;
import geocat.eventprocessor.processors.GetRecordsResult;
import geocat.events.Event;
import geocat.events.actualRecordCollection.EndpointHarvestComplete;
import geocat.events.actualRecordCollection.GetRecordsCommand;
import geocat.model.GetRecordsResponseInfo;
import geocat.service.GetRecordsResponseEvaluator;
import geocat.service.MetadataExploderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
@Scope("prototype")
public class EventProcessor_GetRecordsCommand extends BaseEventProcessor<GetRecordsCommand> {

    Logger logger = LoggerFactory.getLogger(EventProcessor_GetRecordsCommand.class);

    @Autowired
    CSWService cswService;

    @Autowired
    CSWGetRecordsHandler cswGetRecordsHandler;

    @Autowired
    RecordSetService recordSetService;

    @Autowired
    HarvestJobService harvestJobService;

    @Autowired
    EndpointJobService endpointJobService;

    @Autowired
    MetadataExploderService metadataExploderService;

    @Autowired
    GetRecordsResponseEvaluator getRecordsResponseEvaluator;

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

        GetRecordsResponseInfo info = new GetRecordsResponseInfo(xmlParsed);
        HarvestJob harvestJob = harvestJobService.getById(e.getHarvesterId());
        EndpointJob endpointJob = endpointJobService.getById(e.getEndPointId());
        getRecordsResponseEvaluator.evaluate(harvestJob, endpointJob, info);

        int nrecords = cswGetRecordsHandler.extractActualNumberOfRecordsReturned(xmlParsed);
        int nextRecordNumber = cswGetRecordsHandler.extractNextRecordNumber(xmlParsed); // we could test to see if this is 0 if this is the lastone (but this brittle)
        int totalExpectedResults = cswGetRecordsHandler.extractTotalNumberOfRecords(xmlParsed);

        // we requested (say, 20) records, but didn't get back 20 records
        if (nrecords != e.expectedNumberOfRecords())
            throw new Exception("got " + nrecords + ", but expected " + e.expectedNumberOfRecords()); // TODO: might not want to throw

        // totalExpectedResults has changed during the harvest -- indicates an index change
        if (totalExpectedResults != e.getTotalRecordsInQuery()) {
            throw new Exception("totalExpectedResults changed during harvest - index change?");
        }

        if (getInitiatingEvent().isLastSet()) {
            //this ought to be the last one - so, the nextRecordNumber should be 0
            if (nextRecordNumber != 0) {
                logger.debug("expected the last set to return nextRecordNumber=0, but got " + nextRecordNumber +" -- assuming this is a server implementation error and ignoring");
               // throw new Exception("last recordSet - expected nextRecordNumber=0, but got " + nextRecordNumber);
            }
        } else {
            int computedNextRecord = e.getStartRecordNumber()+nrecords;
            if (nextRecordNumber != computedNextRecord)
                throw new Exception("computed NextRecord != received NextRecordNumber - "+computedNextRecord+" != "+nextRecordNumber);
        }
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
