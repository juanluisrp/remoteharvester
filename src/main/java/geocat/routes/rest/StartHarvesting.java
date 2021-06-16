package geocat.routes.rest;

import geocat.events.EventService;
import geocat.model.HarvesterConfig;
import geocat.routes.queuebased.MainOrchestrator;
import org.apache.camel.BeanScope;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.stereotype.Component;


//curl -X POST "http://localhost:9999/api/startHarvest" -H "Content-Type: application/json"  -d '{"url":"http://mapy.geoportal.gov.pl/wss/service/CSWINSP/guest/CSWStartup"}'@Component
@Component
public class StartHarvesting extends RouteBuilder {


    // This route takes an incoming request for starting a Harvesting job.
    //  - a uuid is setup for this harvest job
    //  - a message is put on the startHarvestRequest queue (includes the harvester config and the job uuid)
    //  - the UUID is returned to the user (for further tracking)
    //
    // NOTE: when this returns, the harvest job has NOT been started - its just been scheduled for starting.
    //       Immediately requesting a status of the job after this may result in an error (wait a few seconds)
    @Override
    public void configure() throws Exception {

        restConfiguration().component("jetty").host("localhost").port(9999);

        JacksonDataFormat jsonDefHarvesterConfig = new JacksonDataFormat(HarvesterConfig.class);

        //--- incoming start process request (HTTP)
        rest("/api/startHarvest")
                .post()
                .route()
                .unmarshal(jsonDefHarvesterConfig)  // parse the json input (in post body)
                .routeId("rest.rest.startHarvest")
                .bean(EventService.class, "validateHarvesterConfig", BeanScope.Request)  // have config validate itself
                .bean(EventService.class, "addGUID", BeanScope.Request)  // strip off header and add the process ID guid

                .multicast()
                // send event to start process
                .to(ExchangePattern.InOnly, "direct:addToQueue") // add to queue (see below)

                //return answer (guid) via http
                .setHeader("content-type", constant("application/json"))
                .bean(EventService.class, "resultJSON", BeanScope.Request)
        ;

        // mini-route to send to the message queue
        from("direct:addToQueue") // from above route
                .routeId("rest.HarvestRequestedEvent")

                .log("REST - generating HarvestRequestedEvent")
                .bean(EventService.class, "createHarvestRequestedEvent(${body},${headers.processID})", BeanScope.Request)
                .marshal().json() // convert HarvesterConfig back to json
                .to("activemq:" + MainOrchestrator.myJMSQueueName) //send to message queue
        ;
    }


}
