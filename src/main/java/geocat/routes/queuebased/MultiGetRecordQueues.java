package geocat.routes.queuebased;

import geocat.eventprocessor.EventProcessorRouteCreator;
import geocat.events.actualRecordCollection.GetRecordsCommand;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MultiGetRecordQueues extends SpringRouteBuilder {

    @Autowired
    EventProcessorRouteCreator eventProcessorRouteCreator;

    public static List<String> allQueueGroupNames = new ArrayList<>();

    @Override
    public void configure() throws Exception {
        allQueueGroupNames.add("GET_RECORDS_QUEUE_A_");
        allQueueGroupNames.add("GET_RECORDS_QUEUE_B_");
        allQueueGroupNames.add("GET_RECORDS_QUEUE_PARALLEL_2_");
        allQueueGroupNames.add("GET_RECORDS_QUEUE_PARALLEL_3_");

        for(int t=0;t<10;t++){
            create("GET_RECORDS_QUEUE_A_"+t,1);
        }
        for(int t=0;t<10;t++){
            create("GET_RECORDS_QUEUE_B_"+t,1);
        }
        for(int t=0;t<10;t++){
            create("GET_RECORDS_QUEUE_PARALLEL_2_"+t,2);
        }
        for(int t=0;t<10;t++){
            create("GET_RECORDS_QUEUE_PARALLEL_3_"+t,3);
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
