package net.geocat.eventprocessor;

 import net.geocat.events.Event;
 import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class EventProcessorFactory {

    static String[] subpackages = new String[]{  "main","findlinks","investigatelinks"};

    @Autowired
    BeanFactory beanFactory;

    public static Class processorClass(Class eventType) throws Exception {
        for (String packageName : subpackages) {
            try {
                return Class.forName("net.geocat.eventprocessor.processors." + packageName + ".EventProcessor_" + eventType.getSimpleName());
            } catch (ClassNotFoundException e) {
                //do nothing
            }
        }
        throw new Exception("couldnt find claass - net.geocat.eventprocessor.processors.EventProcessor_" + eventType.getSimpleName());
    }

    public Object create(Event event) throws Exception {
        Class eventType = event.getClass();

        BaseEventProcessor ep = (BaseEventProcessor) beanFactory.getBean(processorClass(eventType));
        ep.setInitiatingEvent(event);
        return ep;
    }

}
