package net.geocat.routes.queuebased;

import net.geocat.eventprocessor.MainLoopRouteCreator;
import net.geocat.eventprocessor.RedirectEvent;
import net.geocat.events.findlinks.LinksFoundInAllDocuments;
 import net.geocat.events.findlinks.ProcessMetadataDocumentEvent;
import net.geocat.events.findlinks.StartProcessDocumentsEvent;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class FindLinksOrchestrator extends SpringRouteBuilder {

    public static String myJMSQueueName = "linkCheck.FindLinksOrchestrator";

    @Autowired
    MainLoopRouteCreator mainLoopRouteCreator;

    @Override
    public void configure() throws Exception {

        mainLoopRouteCreator.createEventProcessingLoop(this,
                "activemq:" + myJMSQueueName,
                new Class[]{StartProcessDocumentsEvent.class, ProcessMetadataDocumentEvent.class},
                Arrays.asList(
                        new RedirectEvent(LinksFoundInAllDocuments.class, "activemq:" + MainOrchestrator.myJMSQueueName)
                ),
                Arrays.asList(new Class[0])
        );
    }
}
