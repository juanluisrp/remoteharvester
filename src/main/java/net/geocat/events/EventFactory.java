package net.geocat.events;

import net.geocat.events.findlinks.StartProcessDocumentsEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class EventFactory {


    public StartProcessDocumentsEvent createStartProcessDocumentsEvent(LinkCheckRequestedEvent linkCheckRequestedEvent){
        StartProcessDocumentsEvent result = new StartProcessDocumentsEvent(
                linkCheckRequestedEvent.getLinkCheckJobId(), linkCheckRequestedEvent.getHarvestJobId()
        );
        return result;
    }

}
