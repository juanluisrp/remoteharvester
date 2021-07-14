package net.geocat.events.processlinks;

import net.geocat.events.Event;

public class ProcessLinkEvent extends Event {

    private long   linkId;

    public ProcessLinkEvent() {}

    public ProcessLinkEvent(long   linkId) {
        this.linkId = linkId;
    }

    public long getLinkId() {
        return linkId;
    }

    public void setLinkId(long linkId) {
        this.linkId = linkId;
    }

    @Override
    public String toString() {
        return "ProcessLinkEvent  - linkId:"+linkId;
    }
}
