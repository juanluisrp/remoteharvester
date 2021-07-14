package net.geocat.eventprocessor;


import net.geocat.events.Event;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class BaseEventProcessor<T extends Event> {

    public T initiatingEvent;
    public Instant whenCreated;

    public BaseEventProcessor() {
        whenCreated = Instant.now();
    }

    public T setInitiatingEvent(T initiatingEvent) {
        this.initiatingEvent = initiatingEvent;
        return initiatingEvent;
    }

    public T getInitiatingEvent() {
        return initiatingEvent;
    }

    public BaseEventProcessor<T> externalProcessing() throws Exception {
        return this;
    }


    public BaseEventProcessor<T> internalProcessing() throws Exception {
        return this;
    }

    public List<Event> newEventProcessing() throws Exception {
        return new ArrayList<>();
    }

}
