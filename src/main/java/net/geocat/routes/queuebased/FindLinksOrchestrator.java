package net.geocat.routes.queuebased;

import net.geocat.eventprocessor.MainLoopRouteCreator;
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
                new Class[]{StartProcessDocumentsEvent.class},
                Arrays.asList(
                       // new RedirectEvent(WorkedDeterminedFinished.class, "activemq:" + MainOrchestrator.myJMSQueueName)
                ),
                Arrays.asList(new Class[0])
        );
    }
}
