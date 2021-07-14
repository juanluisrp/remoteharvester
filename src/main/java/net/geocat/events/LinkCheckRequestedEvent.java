package net.geocat.events;

public class LinkCheckRequestedEvent extends Event {

    private String linkCheckJobId;
    private String harvestJobId;

    public LinkCheckRequestedEvent( ) {
    }

    public LinkCheckRequestedEvent( String linkCheckJobId,String harvestJobId){
        this.harvestJobId = harvestJobId;
        this.linkCheckJobId = linkCheckJobId;
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
    public String toString() {
        return "LinkCheckRequestedEvent - linkCheckJobId:"+getLinkCheckJobId()+", harvestJobId:"+getHarvestJobId();
    }
}
