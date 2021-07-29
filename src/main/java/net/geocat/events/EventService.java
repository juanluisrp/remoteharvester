package net.geocat.events;

import net.geocat.database.harvester.repos.HarvestJobRepo;
import net.geocat.database.linkchecker.repos2.LinkCheckJobRepo;
import net.geocat.model.LinkCheckRunConfig;
import org.apache.camel.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope("prototype")
public class EventService {

    @Autowired
    LinkCheckJobRepo linkCheckJobRepo;

    @Autowired
    HarvestJobRepo harvestJobRepo;

    public void validateLinkCheckJobConfig(Message message) throws Exception {
        ((LinkCheckRunConfig) message.getBody()).validate(linkCheckJobRepo,harvestJobRepo);
    }

    //creates a new GUID
    public String createGUID() {
        UUID guid = java.util.UUID.randomUUID();
        return guid.toString();
    }

    /**
     * remove all headers from the request
     * add processID=GUID  to be used for this harvest
     */
    public void addGUID(Message message) {
        message.getHeaders().clear();
        String guid = createGUID();
        message.getHeaders().put("processID", guid);
        message.getHeaders().put("JMSCorrelationID", guid);
        ((LinkCheckRunConfig) message.getBody()).setProcessID(guid);
    }


    //we are doing trivial JSON conversion
    //   take the processID from the header, and return it as a json string like;
    //{
    //     "processID":"5fcd5f22-1a40-4712-8d2d-ca88c2d0d472"
    //}
    public void resultJSON(Message message) {
        String uuid = ((LinkCheckRunConfig) message.getBody()).getProcessID();
        message.setBody("{\n     \"processID\":\"" + uuid + "\"\n}\n");
    }


    public LinkCheckRequestedEvent createHarvestRequestedEvent(LinkCheckRunConfig linkCheckRunConfig, String processID) {
        LinkCheckRequestedEvent result = new LinkCheckRequestedEvent(processID,linkCheckRunConfig.getHarvestJobId());
        return result;
    }

}
