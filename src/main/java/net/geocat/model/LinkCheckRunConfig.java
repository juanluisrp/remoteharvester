package net.geocat.model;

import net.geocat.database.harvester.entities.HarvestJob;
import net.geocat.database.harvester.repos.HarvestJobRepo;
import net.geocat.database.linkchecker.entities.LinkCheckJob;
import net.geocat.database.linkchecker.repos.LinkCheckJobRepo;

import java.util.Optional;

public class LinkCheckRunConfig {

     String harvestJobId;

    // GUID for the harvest (used as JMS Correlation ID).  Provided by server (do not specify)
    private String processID;

    public void validate(LinkCheckJobRepo linkCheckJobRepo, HarvestJobRepo harvestJobRepo) throws Exception {
        if ( (harvestJobId == null) ||  (harvestJobId.isEmpty()))
            throw new Exception("LinkCheckRunConfig - harvestJobId is empty!");
        Optional<HarvestJob> job = harvestJobRepo.findById(harvestJobId);
        if (!job.isPresent())
            throw  new Exception("LinkCheckRunConfig - cannot find previous harvest run harvestJobId: "+ harvestJobId);
    }

    public String getHarvestJobId() {
        return harvestJobId;
    }

    public void setHarvestJobId(String harvestJobId) {
        this.harvestJobId = harvestJobId;
    }

    public String getProcessID() {
        return processID;
    }

    public void setProcessID(String processID) {
        this.processID = processID;
    }
}
