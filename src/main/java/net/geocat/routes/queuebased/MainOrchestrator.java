package net.geocat.routes.queuebased;

import net.geocat.database.linkchecker.service.DatabaseUpdateService;
import net.geocat.eventprocessor.MainLoopRouteCreator;
import net.geocat.eventprocessor.RedirectEvent;
import net.geocat.events.LinkCheckRequestedEvent;
import net.geocat.events.findlinks.LinksFoundInAllDocuments;
import net.geocat.events.findlinks.StartProcessDocumentsEvent;
import net.geocat.database.linkchecker.service.LinkCheckJobService;
import net.geocat.events.processlinks.ProcessLinkEvent;
import org.apache.camel.BeanScope;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class MainOrchestrator extends SpringRouteBuilder {

    public static String myJMSQueueName = "linkCheck.MainOrchestrator";
    @Autowired
    MainLoopRouteCreator mainLoopRouteCreator;

    @Override
    public void configure() throws Exception {


        mainLoopRouteCreator.createEventProcessingLoop(this,
                "activemq:" + myJMSQueueName,
                new Class[]{  LinkCheckRequestedEvent.class, LinksFoundInAllDocuments.class},
                Arrays.asList(
                        new RedirectEvent(StartProcessDocumentsEvent.class, "activemq:" + FindLinksOrchestrator.myJMSQueueName)
                        ,new RedirectEvent(ProcessLinkEvent.class, "activemq:" + ProcessLinksOrchestrator.myJMSQueueName)
                ),
                Arrays.asList(new Class[0])
        );

        from("activemq:ActiveMQ.DLQ")
                .routeId("MainOrchestrator.DLQ")
                .onException(Exception.class).onExceptionOccurred(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                Exception ex = exchange.getException();
                exchange.getMessage().setHeader("exception", ex);
                exchange.getMessage().setHeader("exceptionTxt", DatabaseUpdateService.convertToString(ex));
            }
        })
                .handled(true)
                .to("activemq:ActiveMQ.DLQ_DLQ")
                .end()

                .bean(LinkCheckJobService.class, "updateLinkCheckJobStateInDBToError( ${header.processID} )", BeanScope.Request)
        ;
    }
}
