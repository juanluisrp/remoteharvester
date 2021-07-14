package net.geocat.events.findlinks;

import net.geocat.events.Event;

public class StartProcessDocumentsEvent extends Event {

    private String linkCheckJobId;
    private String harvestJobId;

    public StartProcessDocumentsEvent(){}

    public StartProcessDocumentsEvent(String linkCheckJobId, String harvestJobId) {
        this.linkCheckJobId = linkCheckJobId;
        this.harvestJobId = harvestJobId;
    }


    public String getLinkCheckJobId() {
        return linkCheckJobId;
    }

    public void setLinkCheckJobId(String linkCheckJobId) {
        this.linkCheckJobId = linkCheckJobId;
    }

    public String getHarvestJobId() {
        return harvestJobId;
    }

    public void setHarvestJobId(String harvestJobId) {
        this.harvestJobId = harvestJobId;
    }

    @Override
    public String toString(){
        return "StartProcessDocumentsEvent - linkCheckJobId:"+linkCheckJobId+", harvestJobId:"+harvestJobId;
    }
}
