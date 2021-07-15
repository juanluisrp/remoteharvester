package net.geocat.routes.queuebased;


import net.geocat.eventprocessor.MainLoopRouteCreator;
import net.geocat.eventprocessor.RedirectEvent;
import net.geocat.events.findlinks.LinksFoundInAllDocuments;
 import net.geocat.events.findlinks.ProcessMetadataDocumentEvent;
import net.geocat.events.findlinks.StartProcessDocumentsEvent;
import net.geocat.events.processlinks.AllLinksCheckedEvent;
import net.geocat.events.processlinks.ProcessLinkEvent;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ProcessLinksOrchestrator extends SpringRouteBuilder {
    public static String myJMSQueueName = "linkCheck.ProcessLinksOrchestrator";

    @Autowired
    MainLoopRouteCreator mainLoopRouteCreator;

    @Override
    public void configure() throws Exception {

        mainLoopRouteCreator.createEventProcessingLoop(this,
                "activemq:" + myJMSQueueName,
                new Class[]{ProcessLinkEvent.class},
                Arrays.asList(
                     new RedirectEvent(AllLinksCheckedEvent.class, "activemq:" + MainOrchestrator.myJMSQueueName)
                ),
                Arrays.asList(new Class[0])
        );
    }
 }
