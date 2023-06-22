package com.geocat.ingester.routes.rest;

import com.geocat.ingester.service.GetStatusService;
import org.apache.camel.BeanScope;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class GetStatus extends RouteBuilder {
    @Value("${app.jettyHost}")
    public String jettyHost;

    @Value("${app.jettyPort}")
    public Integer jettyPort;

    @Override
    public void configure() throws Exception {
        restConfiguration().component("jetty").host(jettyHost).port(jettyPort);

        //--- incoming start process request (HTTP)
        rest("/api/getstatus/")
                .get("/{processID}")
                .route()
                .routeId("rest.rest.getstatus")
                .bean(GetStatusService.class, "getStatus( ${header.processID} )", BeanScope.Request)

                .setHeader("content-type", constant("application/json"))
                .marshal().json()

        ;
    }
}
