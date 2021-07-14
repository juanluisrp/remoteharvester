package net.geocat.eventprocessor.processors.findlinks;

import net.geocat.database.linkchecker.entities.Link;
import net.geocat.database.linkchecker.entities.MetadataDocument;
import net.geocat.database.linkchecker.entities.MetadataDocumentState;
import net.geocat.database.linkchecker.service.MetadataDocumentService;
import net.geocat.eventprocessor.BaseEventProcessor;
import net.geocat.events.Event;
import net.geocat.events.EventFactory;
import net.geocat.events.findlinks.LinksFoundInAllDocuments;
import net.geocat.events.findlinks.MetadataDocumentProcessedEvent;
import net.geocat.events.findlinks.ProcessMetadataDocumentEvent;
import net.geocat.xml.XmlMetadataDocument;
import net.geocat.xml.XmlServiceRecordDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class EventProcessor_MetadataDocumentProcessedEvent extends BaseEventProcessor<MetadataDocumentProcessedEvent> {

    Logger logger = LoggerFactory.getLogger(EventProcessor_MetadataDocumentProcessedEvent.class);

    @Autowired
    MetadataDocumentService metadataDocumentService;

    @Autowired
    EventFactory eventFactory;

    @Override
    public EventProcessor_MetadataDocumentProcessedEvent externalProcessing() throws Exception {
        return this;
    }


    @Override
    public EventProcessor_MetadataDocumentProcessedEvent internalProcessing() throws Exception {
        return this;
    }


    @Override
    public List<Event> newEventProcessing() {
        List<Event> result = new ArrayList<>();

//        if (metadataDocumentService.complete(getInitiatingEvent().getLinkCheckJobId()))
//        {
//            LinksFoundInAllDocuments e = eventFactory.createLinksFoundInAllDocuments(getInitiatingEvent());
//            result.add(e);
//        }

        return result;
    }
}
