package net.geocat.events;

import net.geocat.database.harvester.entities.MetadataRecord;
import net.geocat.database.linkchecker.entities.Link;
import net.geocat.events.findlinks.LinksFoundInAllDocuments;
 import net.geocat.events.findlinks.ProcessMetadataDocumentEvent;
import net.geocat.events.findlinks.StartProcessDocumentsEvent;
import net.geocat.events.processlinks.AllLinksCheckedEvent;
import net.geocat.events.processlinks.ProcessLinkEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class EventFactory {


    public StartProcessDocumentsEvent createStartProcessDocumentsEvent(LinkCheckRequestedEvent linkCheckRequestedEvent){
        StartProcessDocumentsEvent result = new StartProcessDocumentsEvent(
                linkCheckRequestedEvent.getLinkCheckJobId(), linkCheckRequestedEvent.getHarvestJobId()
        );
        return result;
    }

    public ProcessMetadataDocumentEvent createProcessMetadataDocumentEvent(String linkCheckJobId,String harvestJobId, long endpointJobId,String sha2) {
        ProcessMetadataDocumentEvent result = new ProcessMetadataDocumentEvent( linkCheckJobId,harvestJobId,endpointJobId,  sha2);
        return result;
    }

//    public MetadataDocumentProcessedEvent createMetadataDocumentProcessedEvent(ProcessMetadataDocumentEvent e  ) {
//        MetadataDocumentProcessedEvent result = new MetadataDocumentProcessedEvent(
//                e.getLinkCheckJobId(), e.getHarvestJobId(), e.getEndpointJobId(), e.getSha2() );
//        return result;
//    }

    public LinksFoundInAllDocuments createLinksFoundInAllDocuments(ProcessMetadataDocumentEvent initiatingEvent) {
        LinksFoundInAllDocuments result = new LinksFoundInAllDocuments(initiatingEvent.getLinkCheckJobId(),initiatingEvent.getHarvestJobId());
        return result;
    }

    public ProcessLinkEvent createProcessLinkEvent(Link l) {
        ProcessLinkEvent result = new ProcessLinkEvent(l.getLinkId(),l.getLinkCheckJobId());
        return result;
    }

    public AllLinksCheckedEvent createAllLinksCheckedEvent(String linkcheckJobId){
        AllLinksCheckedEvent event = new AllLinksCheckedEvent(linkcheckJobId);
        return event;
    }
}
