/*
 *  =============================================================================
 *  ===  Copyright (C) 2021 Food and Agriculture Organization of the
 *  ===  United Nations (FAO-UN), United Nations World Food Programme (WFP)
 *  ===  and United Nations Environment Programme (UNEP)
 *  ===
 *  ===  This program is free software; you can redistribute it and/or modify
 *  ===  it under the terms of the GNU General Public License as published by
 *  ===  the Free Software Foundation; either version 2 of the License, or (at
 *  ===  your option) any later version.
 *  ===
 *  ===  This program is distributed in the hope that it will be useful, but
 *  ===  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  ===  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  ===  General Public License for more details.
 *  ===
 *  ===  You should have received a copy of the GNU General Public License
 *  ===  along with this program; if not, write to the Free Software
 *  ===  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 *  ===
 *  ===  Contact: Jeroen Ticheler - FAO - Viale delle Terme di Caracalla 2,
 *  ===  Rome - Italy. email: geonetwork@osgeo.org
 *  ===
 *  ===  Development of this program was financed by the European Union within
 *  ===  Service Contract NUMBER – 941143 – IPR – 2021 with subject matter
 *  ===  "Facilitating a sustainable evolution and maintenance of the INSPIRE
 *  ===  Geoportal", performed in the period 2021-2023.
 *  ===
 *  ===  Contact: JRC Unit B.6 Digital Economy, Via Enrico Fermi 2749,
 *  ===  21027 Ispra, Italy. email: JRC-INSPIRE-SUPPORT@ec.europa.eu
 *  ==============================================================================
 */

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
                .bean(EventService.class, "resultJSON", BeanScope.Request)
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
