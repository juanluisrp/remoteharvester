package net.geocat.routes.rest;

import net.geocat.events.EventService;
import net.geocat.model.LinkCheckRunConfig;
import net.geocat.routes.queuebased.MainOrchestrator;
import org.apache.camel.BeanScope;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StartLinkCheck extends RouteBuilder {


    @Value("${geocat.jettyHost}")
    public String jettyHost;

    @Value("${geocat.jettyPort}")
    public Integer jettyPort;

    @Override
    public void configure() throws Exception {
        restConfiguration().component("jetty").host(jettyHost).port(jettyPort);

        JacksonDataFormat jsonDefLinkCheckRunConfig = new JacksonDataFormat(LinkCheckRunConfig.class);

        rest("/api/startLinkCheck")
                .post()
                .route()
                .unmarshal(jsonDefLinkCheckRunConfig)  // parse the json input (in post body)
                .routeId("rest.rest.StartLinkCheck")
                .bean(EventService.class, "validateLinkCheckJobConfig", BeanScope.Request)  // have config validate itself
                .bean(EventService.class, "addGUID", BeanScope.Request)  // strip off header and add the process ID guid
                .multicast()
                // send event to start process
                .to(ExchangePattern.InOnly, "direct:addToQueue") // add to queue (see below)

                //return answer (guid) via http
                .setHeader("content-type", constant("application/json"))
                .bean(EventService.class,"resultJSON", BeanScope.Request)
        ;
        // mini-route to send to the message queue
        from("direct:addToQueue") // from above route
                .routeId("rest.LinkCheckRequestedEvent")

                .log("REST - generating LinkCheckRequestedEvent")
                .bean(EventService.class, "createHarvestRequestedEvent(${body},${headers.processID})", BeanScope.Request)
                .log("${body}")
                .marshal().json() // convert HarvesterConfig back to json
                .to("activemq:" + MainOrchestrator.myJMSQueueName) //send to message queue
        ;
    }
}
