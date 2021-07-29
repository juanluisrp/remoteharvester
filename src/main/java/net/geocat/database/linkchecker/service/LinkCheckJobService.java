package net.geocat.database.linkchecker.service;

import net.geocat.database.linkchecker.entities2.LinkCheckJob;
import net.geocat.database.linkchecker.entities2.LinkCheckJobState;
import net.geocat.database.linkchecker.repos2.LinkCheckJobRepo;
import net.geocat.events.LinkCheckRequestedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Scope("prototype")
public class LinkCheckJobService {

    @Autowired
    LinkCheckJobRepo linkCheckJobRepo;


    public LinkCheckJob updateLinkCheckJobStateInDBToError(String guid) throws Exception {
        return updateLinkCheckJobStateInDB(guid, LinkCheckJobState.ERROR);
    }

    public LinkCheckJob updateLinkCheckJobStateInDB(String guid, LinkCheckJobState state) {
//        LinkCheckJob job = linkCheckJobRepo.findById(guid).get();
//        job.setState(state);
//        return linkCheckJobRepo.save(job);
        return null;
    }

    public LinkCheckJob createLinkCheckJobInDB(LinkCheckRequestedEvent event) {
//        Optional<LinkCheckJob> job = linkCheckJobRepo.findById(event.getLinkCheckJobId());
//        if (job.isPresent()) //2nd attempt
//        {
//            job.get().setState(LinkCheckJobState.CREATING);
//            return linkCheckJobRepo.save(job.get());
//        }
//        LinkCheckJob newJob = new LinkCheckJob();
//        newJob.setJobId(event.getLinkCheckJobId());
//        newJob.setHarvestJobId(event.getHarvestJobId());
//        return linkCheckJobRepo.save(newJob);
        return null;
    }

}
