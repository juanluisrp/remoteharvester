package geocat.routes.rest;

import geocat.model.HarvesterConfig;
import geocat.service.GetStatusService;
import org.apache.camel.BeanScope;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class GetStatus extends RouteBuilder {

    @Value("${geocat.jettyHost}")
    public String jettyHost;

    @Value("${geocat.jettyPort}")
    public Integer jettyPort;

    @Override
    public void configure() throws Exception {
        restConfiguration().component("jetty").host(jettyHost).port(jettyPort);

        JacksonDataFormat jsonDefHarvesterConfig = new JacksonDataFormat(HarvesterConfig.class);

        //--- incoming start process request (HTTP)
        rest("/api/getstatus/")
                .get("/{processID}")
                .route()
                .routeId("rest.rest.getstatus")
                .bean(GetStatusService.class, "getStatus( ${header.processID}, ${header.quick} )", BeanScope.Request)

                .setHeader("content-type", constant("application/json"))
                .marshal().json()

        ;
    }
}
