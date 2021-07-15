package net.geocat.events.processlinks;

import net.geocat.events.Event;

public class ProcessLinkEvent extends Event {

    private long   linkId;
    private String  linkCheckJobId;

    public ProcessLinkEvent() {}

    public ProcessLinkEvent(long linkId,String  linkCheckJobId) {
        this.linkId = linkId;
        this.linkCheckJobId = linkCheckJobId;
    }

    public long getLinkId() {
        return linkId;
    }

    public void setLinkId(long linkId) {
        this.linkId = linkId;
    }

    public String getLinkCheckJobId() {
        return linkCheckJobId;
    }

    public void setLinkCheckJobId(String linkCheckJobId) {
        this.linkCheckJobId = linkCheckJobId;
    }

    @Override
    public String toString() {
        return "ProcessLinkEvent  - linkId:"+linkId+", linkcheckJobId:"+linkCheckJobId;
    }
}
