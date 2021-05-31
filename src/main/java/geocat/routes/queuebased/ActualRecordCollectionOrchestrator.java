package geocat.routes.queuebased;

import geocat.eventprocessor.MainLoopRouteCreator;
import geocat.eventprocessor.RedirectEvent;
import geocat.events.*;
import geocat.events.actualRecordCollection.*;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

 import java.util.ArrayList;
import java.util.Arrays;


@Component
public class ActualRecordCollectionOrchestrator extends SpringRouteBuilder {

    @Autowired
    MainLoopRouteCreator mainLoopRouteCreator;

    public static String myJMSQueueName = "harvest.ActualRecordCollectEvents";

    @Override
    public void configure() throws Exception {

        mainLoopRouteCreator.createEventProcessingLoop(this,
                "activemq:"+myJMSQueueName,
                new Class[] {ActualHarvestStartCommand.class, ActualHarvestEndpointStartCommand.class, GetRecordsCommand.class, EndpointHarvestComplete.class},
                Arrays.asList(new RedirectEvent(ActualHarvestCompleted.class,"activemq:harvest.mainOrchestrator.events"))
                );
    }
}
