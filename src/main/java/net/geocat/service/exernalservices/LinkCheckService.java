package net.geocat.service.exernalservices;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.geocat.database.linkchecker.entities.helper.HttpResult;
import net.geocat.http.BasicHTTPRetriever;
import net.geocat.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class LinkCheckService {

    Logger logger = LoggerFactory.getLogger(LinkCheckService.class);


    @Autowired
    BasicHTTPRetriever basicHTTPRetriever;

    ObjectMapper objectMapper = new ObjectMapper();

    @Value("${linkchecker.url}")
    String linkcheckerAPIURL;


    // call the harvest remote service and return the processID
    public HarvestStartResponse startLinkCheck(String harvesterId) throws  Exception {

        String url = linkcheckerAPIURL+"/startLinkCheck";
        LinkCheckRunConfig linkCheckRunConfig = new LinkCheckRunConfig();
        linkCheckRunConfig.setHarvestJobId(harvesterId);
        String requestJSON = objectMapper.writeValueAsString(linkCheckRunConfig);

        HttpResult httpResponse = sendJSON("POST",url, requestJSON);
        String result = httpResponse.getData() == null ? "" : new String(httpResponse.getData());
        if (httpResponse.isErrorOccurred() || (httpResponse.getHttpCode() != 200))
            throw new Exception("couldnt start linkCheck process - "+result);

        HarvestStartResponse _result = objectMapper.readValue(result, HarvestStartResponse.class);
        logger.debug("started linkcheck with ProcessId="+_result.getProcessID());

        return _result;
    }

    public LinkCheckStatus getLinkCheckState(String linkCheckProcessID) throws Exception {
        String url = linkcheckerAPIURL+"/getstatus/"+linkCheckProcessID;
        HttpResult httpResponse = sendJSON("GET",url,null);
        String result = httpResponse.getData() == null ? "" : new String(httpResponse.getData());
        if (httpResponse.isErrorOccurred() || (httpResponse.getHttpCode() != 200))
            throw new Exception("couldnt get harvest process state - "+result);

        LinkCheckStatus _result  =  objectMapper.readValue(result, LinkCheckStatus.class);
        logger.debug("linkcheck state="+computeLinkCheckState(_result));

        return _result;
    }

    public String computeLinkCheckState(LinkCheckStatus status) {
        String result = "LinkCheck "+status.getProcessID()+" is in State:"+status.getLinkCheckJobState();

        if (status.getServiceRecordStatus() != null) {
            DocumentTypeStatus service = status.getServiceRecordStatus();
            result += "\n   + "+service.getnTotalDocuments()+" service documents";
            for(StatusType st : service.getStatusTypes()) {
                result += "\n         +"+st.getnDocuments()+" in state "+st.getStatusType();

            }
        }


        if (status.getDatasetRecordStatus() != null) {
            DocumentTypeStatus ds = status.getDatasetRecordStatus();
            result += "\n   + "+ds.getnTotalDocuments()+" dataset documents";
            for(StatusType st : ds.getStatusTypes()) {
                result += "\n         +"+st.getnDocuments()+" in state "+st.getStatusType();

            }
        }

        return result;
    }

    public HttpResult sendJSON(String verb, String url, String json) throws  Exception {
        HttpResult result = basicHTTPRetriever.retrieveJSON(verb, url, json, null,null);
        return result;
    }

}
