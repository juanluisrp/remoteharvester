package geocat.routes.queuebased;

import geocat.database.service.HarvestJobService;
import geocat.eventprocessor.MainLoopRouteCreator;
import geocat.eventprocessor.RedirectEvent;
import geocat.events.HarvestRequestedEvent;
import geocat.events.actualRecordCollection.ActualHarvestCompleted;
import geocat.events.actualRecordCollection.ActualHarvestStartCommand;
import geocat.events.determinework.DetermineWorkStartCommand;
import geocat.events.determinework.WorkedDeterminedFinished;
import org.apache.camel.BeanScope;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;

@Component
public class MainOrchestrator extends SpringRouteBuilder {

    @Autowired
    MainLoopRouteCreator mainLoopRouteCreator;


    public static String myJMSQueueName = "harvest.MainOrchestrator";

    @Override
    public void configure() throws Exception {



        mainLoopRouteCreator.createEventProcessingLoop(this,
                "activemq:" + myJMSQueueName,
                new Class[]{HarvestRequestedEvent.class, WorkedDeterminedFinished.class, ActualHarvestCompleted.class},
                Arrays.asList(
                        new RedirectEvent(DetermineWorkStartCommand.class,"activemq:"+DetermineWorkOrchestrator.myJMSQueueName),
                        new RedirectEvent(ActualHarvestStartCommand.class,"activemq:"+ActualRecordCollectionOrchestrator.myJMSQueueName)
                        )
        );


        from( "activemq:ActiveMQ.DLQ")
                .routeId("MainOrchestrator.DLQ")
                .onException(Throwable.class).to("activemq:ActiveMQ.DLQ_DLQ").end()
                .bean(HarvestJobService.class,"updateHarvestJobStateInDB( ${header.processID} ,'ERROR')", BeanScope.Request)
        ;

    }
}
