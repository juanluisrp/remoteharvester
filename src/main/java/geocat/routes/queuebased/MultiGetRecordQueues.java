package geocat.routes.queuebased;

import geocat.eventprocessor.EventProcessorRouteCreator;
import geocat.events.actualRecordCollection.GetRecordsCommand;
import geocat.service.QueueChooserService;
import geocat.service.QueueInfo;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MultiGetRecordQueues extends SpringRouteBuilder {

    @Autowired
    EventProcessorRouteCreator eventProcessorRouteCreator;

    @Autowired
    QueueChooserService queueChooserService;


    @Override
    public void configure() throws Exception {

        for(QueueInfo info : queueChooserService.enumerateAllQueues()){
            create(info.queueName(),info.parallelism());
        }

    }

    private void create(String queueName, int parallelism) throws Exception {
        String queue = "activemq:"+queueName;
        if (parallelism>1)
            queue = queue + "?concurrentConsumers="+parallelism;
        eventProcessorRouteCreator.addEventProcessor(
                this,
                GetRecordsCommand.class,
                queue,
                "activemq:"+ActualRecordCollectionOrchestrator.myJMSQueueName,
                queueName,
                true
                );
    }
}
