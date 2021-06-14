package geocat.eventprocessor;

import geocat.events.Event;
import org.apache.camel.BeanScope;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class EventProcessorRouteCreator {
    static JacksonDataFormat jsonDefHarvesterConfig = new JacksonDataFormat(Event.class);

    /**
     * eventType example - eventType=ActualHarvestEndpointStartCommand
     * Then there should be a class called
     * EventProcessor_ActualHarvestEndpointStartCommand extends BaseEventProcessor<ActualHarvestEndpointStartCommand>
     *
     * @param routeBuilder
     * @param eventType
     * @param from
     * @param to
     * @throws Exception
     */
    public void addEventProcessor(RouteBuilder routeBuilder,
                                  Class eventType,
                                  String from,
                                  String to,
                                  String tag,
                                  boolean unmarshal //will unmarshal message + transacted
    ) throws Exception {
        validate(eventType);


        //=====================================================================
        if (unmarshal)
            routeBuilder
                    .from(from)  // ${body} will be JMS message - unmarshal
                    .transacted()
                    .unmarshal(jsonDefHarvesterConfig)
                    .routeId(tag + "_" + eventType.getSimpleName())
                    .log("processing event of type " + eventType.getSimpleName() + " from " + from)
                    .log("event = ${body}")
                    .bean(EventProcessorFactory.class, "create( ${body} )", BeanScope.Request)
                    .transform().simple("${body.externalProcessing()}")
                    .transform().simple("${body.internalProcessing()}")
                    .transform().simple("${body.newEventProcessing()}")
                    .split().simple("${body}")
                    .marshal().json()
                    .to(to)
                ;
        else
            routeBuilder
                    .from(from)  // ${body} will be of type eventType
                    .routeId(tag + "_" + eventType.getSimpleName())
                    .log("processing event of type " + eventType.getSimpleName() + " from " + from)
                    .bean(EventProcessorFactory.class, "create( ${body} )", BeanScope.Request)
                    .transform().simple("${body.externalProcessing()}")
                    .transform().simple("${body.internalProcessing()}")
                    .transform().simple("${body.newEventProcessing()}")
                    .split().simple("${body}")
                    .marshal().json()
                    .to(to)
                    ;
        //=====================================================================
    }

    public void validate(Class eventType) throws Exception {
        if (!Event.class.isAssignableFrom(eventType))
            throw new Exception("RouteCreator - eventType must be Event");
        EventProcessorFactory.processorClass(eventType);//will throw if not found
    }


}
