package geocat.routes.rest;

import geocat.dblogging.service.GetLogService;
import geocat.model.HarvesterConfig;
import org.apache.camel.BeanScope;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.stereotype.Component;

@Component
public class GetLog extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        restConfiguration().component("jetty").host("localhost").port(9999);

        JacksonDataFormat jsonDefHarvesterConfig = new JacksonDataFormat(HarvesterConfig.class);

        //--- incoming start process request (HTTP)
        rest("/api/getlog/")
                .get("/{processID}")
                .route()
                .routeId("rest.rest.getlog")
                .bean(GetLogService.class, "queryLogByProcessID( ${header.processID} )", BeanScope.Request)

                .setHeader("content-type", constant("application/json"))
                .marshal().json()

        ;
    }
}
