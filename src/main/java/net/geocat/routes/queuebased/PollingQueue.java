package net.geocat.routes.queuebased;

import net.geocat.service.PollingService;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;



@Component
public class PollingQueue extends SpringRouteBuilder {

    public static String myJMSQueueName = "Orchestrator.Polling";

    @Override
    public void configure() throws Exception {
            from("timer://pollingTimer?fixedRate=true&period=30000&delay=3000")
                    .bean(PollingService.class,"ping")
                    .split()
                    .simple("${body}")
                    .marshal().json()
                    .to("activemq:"+MainOrchestrator.myJMSQueueName);
    }
}
