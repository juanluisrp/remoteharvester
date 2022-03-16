package geocat.routes.rest;

import geocat.service.GetHarvestInfoService;
import org.apache.camel.BeanScope;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GetHarvestInfo extends RouteBuilder {
    @Value("${geocat.jettyHost}")
    public String jettyHost;

    @Value("${geocat.jettyPort}")
    public Integer jettyPort;

    @Override
    public void configure() throws Exception {
        restConfiguration().component("jetty").host(jettyHost).port(jettyPort);

        //--- incoming start process request (HTTP)
        rest("/api/getLastCompletedHarvestJobIdByLongTermTag/")
                .get("/{name}")
                .route()
                .routeId("rest.rest.getLastCompletedHarvestJobIdByLongTermTag")
                .bean(GetHarvestInfoService.class, "getLastCompletedHarvestJobIdByLongTermTag( ${header.name} )", BeanScope.Request)

                .setHeader("content-type", constant("application/json"))
                .marshal().json()

        ;
    }
}
