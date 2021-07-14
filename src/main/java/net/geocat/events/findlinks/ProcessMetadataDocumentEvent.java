package net.geocat.events.findlinks;

import net.geocat.events.Event;

public class ProcessMetadataDocumentEvent extends Event {

    private String linkCheckJobId;
    private String sha2;
    private long endpointJobId;
    private String harvestJobId;

    public ProcessMetadataDocumentEvent() {}

    public ProcessMetadataDocumentEvent(String linkCheckJobId, String harvestJobId,long endpointJobId, String sha2) {
        this.linkCheckJobId = linkCheckJobId;
        this.sha2 = sha2;
        this.endpointJobId = endpointJobId;
        this.harvestJobId = harvestJobId;
    }

    public String getLinkCheckJobId() {
        return linkCheckJobId;
    }

    public void setLinkCheckJobId(String linkCheckJobId) {
        this.linkCheckJobId = linkCheckJobId;
    }

    public String getSha2() {
        return sha2;
    }

    public void setSha2(String sha2) {
        this.sha2 = sha2;
    }

    public long getEndpointJobId() {
        return endpointJobId;
    }

    public void setEndpointJobId(long endpointJobId) {
        this.endpointJobId = endpointJobId;
    }

    public String getHarvestJobId() {
        return harvestJobId;
    }

    public void setHarvestJobId(String harvestJobId) {
        this.harvestJobId = harvestJobId;
    }

    @Override
    public String toString(){
        return "ProcessMetadataDocumentEvent - linkCheckJobId:"+linkCheckJobId+",harvestJobId:"+harvestJobId+", endpointjobid:"+endpointJobId+", sha2:"+sha2;
    }
}
