package geocat.routes.queuebased;

import geocat.eventprocessor.MainLoopRouteCreator;
import geocat.eventprocessor.RedirectEvent;
import geocat.events.actualRecordCollection.*;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Component
public class ActualRecordCollectionOrchestrator extends SpringRouteBuilder {

    public static String myJMSQueueName = "harvest.ActualRecordCollectEvents";
    @Autowired
    MainLoopRouteCreator mainLoopRouteCreator;

    @Override
    public void configure() throws Exception {

        mainLoopRouteCreator.createEventProcessingLoop(this,
                "activemq:" + myJMSQueueName,
                new Class[]{ActualHarvestStartCommand.class, ActualHarvestEndpointStartCommand.class, GetRecordsCommand.class, EndpointHarvestComplete.class},
                Arrays.asList(new RedirectEvent(ActualHarvestCompleted.class, "activemq:"+MainOrchestrator.myJMSQueueName))
        );
    }
}
