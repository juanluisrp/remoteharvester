package com.geocat.ingester.eventprocessor;

import com.geocat.ingester.events.Event;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class EventProcessorFactory {

    static String[] subpackages = new String[]{"ingest", "main"};

    @Autowired
    BeanFactory beanFactory;

    public static Class processorClass(Class eventType) throws Exception {
        for (String packageName : subpackages) {
            try {
                return Class.forName("com.geocat.ingester.eventprocessor.processors." + packageName + ".EventProcessor_" + eventType.getSimpleName());
            } catch (ClassNotFoundException e) {
                //do nothing
            }
        }
        throw new Exception("could not find class - com.geocat.eventprocessor.processors.EventProcessor_" + eventType.getSimpleName());
    }

    public Object create(Event event) throws Exception {
        Class eventType = event.getClass();

        BaseEventProcessor ep = (BaseEventProcessor) beanFactory.getBean(processorClass(eventType));
        ep.setInitiatingEvent(event);
        return ep;
    }

}
