package geocat.routes.rest;


import geocat.service.DeleteJobService;
import org.apache.camel.BeanScope;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DeleteJob extends RouteBuilder {


    @Value("${geocat.jettyHost}")
    public String jettyHost;

    @Value("${geocat.jettyPort}")
    public Integer jettyPort;


    @Override
    public void configure() throws Exception {
        restConfiguration().component("jetty").host(jettyHost).port(jettyPort);

        // JacksonDataFormat jsonDefHarvesterConfig = new JacksonDataFormat(HarvesterConfig.class);

        //--- incoming start process request (HTTP)
        rest("/api/deletejob/")
                .delete("/{processID}")
                .route()
                .routeId("rest.rest.deletejob")
                .bean(DeleteJobService.class, "deleteById( ${header.processID} )", BeanScope.Request)

                .setHeader("content-type", constant("application/json"))
                .marshal().json()

        ;

        //--- incoming start process request (HTTP)
        rest("/api/atMostJobs/")
                .delete("/{longTermTag}/{maxAllowed}")
                .route()
                .routeId("rest.rest.atMostJobs")
                .bean(DeleteJobService.class, "ensureAtMost( ${header.longTermTag} ,   ${header.maxAllowed})", BeanScope.Request)

                .setHeader("content-type", constant("application/json"))
                .marshal().json()

        ;
    }
}
