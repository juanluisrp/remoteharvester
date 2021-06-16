package geocat.routes.rest;

import geocat.events.EventFactory;
import geocat.model.HarvesterConfig;
import geocat.routes.queuebased.MainOrchestrator;
import org.apache.camel.BeanScope;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.stereotype.Component;


@Component
public class AbortHarvest extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        restConfiguration().component("jetty").host("localhost").port(9999);

        JacksonDataFormat jsonDefHarvesterConfig = new JacksonDataFormat(HarvesterConfig.class);

        //--- incoming start process request (HTTP)
        rest("/api/abortharvest/")
                .get("/{processID}")
                .route()
                .routeId("rest.rest.abortharvest")
                .setHeader("JMSCorrelationID", simple("${header.processID}"))

                .multicast()
                .to(ExchangePattern.InOnly, "direct:abortharvest_addToQueue")

                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        exchange.getMessage().setHeader("content-type", "application/json");
                        exchange.getMessage().setBody("{\"status\": \"USERABORT\"}");
                    }
                })
        ;

        // mini-route to send to the message queue
        from("direct:abortharvest_addToQueue") // from above route
                .routeId("rest.HarvestAbortEvent")

                .log("REST - generating HarvestAbortEvent")
                .bean(EventFactory.class, "create_HarvestAbortEvent( ${header.processID} )", BeanScope.Request)
                .marshal().json() // convert HarvesterConfig back to json
                .to("activemq:" + MainOrchestrator.myJMSQueueName) //send to message queue
        ;
    }
}
