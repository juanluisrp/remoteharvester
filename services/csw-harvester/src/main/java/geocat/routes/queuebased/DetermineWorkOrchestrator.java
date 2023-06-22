package geocat.routes.queuebased;

import geocat.eventprocessor.MainLoopRouteCreator;
import geocat.eventprocessor.RedirectEvent;
import geocat.events.determinework.CSWEndPointDetectedEvent;
import geocat.events.determinework.CSWEndpointWorkDetermined;
import geocat.events.determinework.DetermineWorkStartCommand;
import geocat.events.determinework.WorkedDeterminedFinished;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DetermineWorkOrchestrator extends SpringRouteBuilder {

    public static String myJMSQueueName = "harvest.DetermineWorkOrchestrator";
    @Autowired
    MainLoopRouteCreator mainLoopRouteCreator;

    @Override
    public void configure() throws Exception {

        mainLoopRouteCreator.createEventProcessingLoop(this,
                "activemq:" + myJMSQueueName,
                new Class[]{DetermineWorkStartCommand.class, CSWEndPointDetectedEvent.class, CSWEndpointWorkDetermined.class},
                Arrays.asList(
                        new RedirectEvent(WorkedDeterminedFinished.class, "activemq:" + MainOrchestrator.myJMSQueueName)
                ),
                Arrays.asList(new Class[0]),
                2
        );
    }
}