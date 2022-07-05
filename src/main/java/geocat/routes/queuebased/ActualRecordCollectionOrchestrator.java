package geocat.routes.queuebased;

import geocat.eventprocessor.MainLoopRouteCreator;
import geocat.eventprocessor.RedirectEvent;
import geocat.events.Event;
import geocat.events.actualRecordCollection.*;
import org.apache.camel.component.jackson.JacksonDataFormat;
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

        JacksonDataFormat jsonDefHarvesterConfig = new JacksonDataFormat(Event.class);


        mainLoopRouteCreator.createEventProcessingLoop(this,
                "activemq:" + myJMSQueueName,
                new Class[]{ActualHarvestStartCommand.class, ActualHarvestEndpointStartCommand.class, GetRecordsCommand.class, EndpointHarvestComplete.class},
                Arrays.asList(new RedirectEvent(ActualHarvestCompleted.class, "activemq:" + MainOrchestrator.myJMSQueueName)),
                Arrays.asList(new Class[]{GetRecordsCommand.class}),
                2
        );

        from("direct:" + myJMSQueueName + "_GetRecordsCommand")
                .routeId(myJMSQueueName + "_GetRecordsCommand")
                .setHeader("workQueueName", simple(" ${body.getWorkQueueName()}"))
                .log("routing to queue: ${body.getWorkQueueName()}")
                .marshal().json()
                .toD("activemq:${header.workQueueName}")
        ;
    }
}
