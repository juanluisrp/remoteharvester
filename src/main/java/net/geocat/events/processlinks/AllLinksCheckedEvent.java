package net.geocat.events.processlinks;

import net.geocat.events.Event;

public class AllLinksCheckedEvent extends Event {

    private String linkCheckJobId;

    public AllLinksCheckedEvent() {}

    public AllLinksCheckedEvent(String linkCheckJobId) {
        this.linkCheckJobId = linkCheckJobId;
    }

    public String getLinkCheckJobId() {
        return linkCheckJobId;
    }

    public void setLinkCheckJobId(String linkCheckJobId) {
        this.linkCheckJobId = linkCheckJobId;
    }
}
