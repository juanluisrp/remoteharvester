package geocat.eventprocessor;

import geocat.events.Event;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class EventProcessorFactory {

    static String[] subpackages = new String[]{"harvest", "determinework", "main"};

    @Autowired
    BeanFactory beanFactory;

    public static Class processorClass(Class eventType) throws Exception {
        for (String packageName : subpackages) {
            try {
                return Class.forName("geocat.eventprocessor.processors." + packageName + ".EventProcessor_" + eventType.getSimpleName());
            } catch (ClassNotFoundException e) {
                //do nothing
            }
        }
        throw new Exception("couldnt find claass - geocat.eventprocessor.processors.EventProcessor_" + eventType.getSimpleName());
    }

    public Object create(Event event) throws Exception {
        Class eventType = event.getClass();

        BaseEventProcessor ep = (BaseEventProcessor) beanFactory.getBean(processorClass(eventType));
        ep.setInitiatingEvent(event);
        return ep;
    }

}
