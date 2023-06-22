package com.geocat.ingester.routes.rest;

import com.geocat.ingester.events.IngestEventService;
import com.geocat.ingester.model.IngesterConfig;
import com.geocat.ingester.routes.queuebased.IngestMainOrchestrator;
import org.apache.camel.BeanScope;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StartIngesting extends RouteBuilder {

    @Value("${app.jettyHost}")
    public String jettyHost;

    @Value("${app.jettyPort}")
    public Integer jettyPort;

    @Override
    public void configure() throws Exception {
        restConfiguration().component("jetty").host(jettyHost).port(jettyPort);

        JacksonDataFormat jsonDefIngesterConfig = new JacksonDataFormat(IngesterConfig.class);


        //--- incoming start process request (HTTP)
        rest("/api/startIngest")
                .post()
                .route()
                //.transacted("PROPAGATION_INGEST_REQUIRED")
                .unmarshal(jsonDefIngesterConfig)  // parse the json input (in post body)
                .routeId("rest.rest.startIngest")
                .bean(IngestEventService.class, "validateIngesterConfig", BeanScope.Request)
                .bean(IngestEventService.class, "addGUID", BeanScope.Request)  // strip off header and add the process ID guid
                .log("${body}")

                .multicast()
                // send event to start process
                .to(ExchangePattern.InOnly, "direct:addToIngestQueue") // add to queue (see below)

                //return answer (guid) via http
                .setHeader("content-type", constant("application/json"))
                .bean(IngestEventService.class, "resultJSON", BeanScope.Request)

        ;

        // mini-route to send to the message queue
        from("direct:addToIngestQueue") // from above route
                //.transacted("PROPAGATION_INGEST_REQUIRED")
                .routeId("rest.IngestRequestedEvent")
                .log("IngestRequestedEvent")
                .bean(IngestEventService.class, "createIngestRequestedEvent(${body},${headers.processID})", BeanScope.Request)
                .marshal().json() // convert HarvesterConfig back to json
                .to("activemq:" + IngestMainOrchestrator.myJMSQueueName) //send to message queue

        ;
    }
}
