package net.geocat.service.exernalservices;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.geocat.database.linkchecker.entities.helper.HttpResult;
import net.geocat.http.BasicHTTPRetriever;
import net.geocat.model.HarvestStartResponse;
import net.geocat.model.IngestStatus;
import net.geocat.model.LinkCheckRunConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class IngesterService {

    @Autowired
    BasicHTTPRetriever basicHTTPRetriever;

    ObjectMapper objectMapper = new ObjectMapper();

    @Value("${ingester.url}")
    String ingesterAPIURL;

    // call the harvest remote service and return the processID
    public HarvestStartResponse startIngest(String harvesterId) throws  Exception {

        String url = ingesterAPIURL+"/startIngest";
        LinkCheckRunConfig linkCheckRunConfig = new LinkCheckRunConfig();
        linkCheckRunConfig.setHarvestProcessID(harvesterId);
        String requestJSON = objectMapper.writeValueAsString(linkCheckRunConfig);

        HttpResult httpResponse = sendJSON("POST",url, requestJSON);
        String result = httpResponse.getData() == null ? "" : new String(httpResponse.getData());
        if (httpResponse.isErrorOccurred() || (httpResponse.getHttpCode() != 200))
            throw new Exception("couldn't start ingest process - "+result);

        HarvestStartResponse _result = objectMapper.readValue(result, HarvestStartResponse.class);

        return _result;
    }

    public IngestStatus getIngestState(String ingesterProcessID) throws Exception {
        String url = ingesterAPIURL+"/getstatus/"+ingesterProcessID;
        HttpResult httpResponse = sendJSON("GET",url,null);
        String result = httpResponse.getData() == null ? "" : new String(httpResponse.getData());
        if (httpResponse.isErrorOccurred() || (httpResponse.getHttpCode() != 200))
            throw new Exception("couldn't get ingest process state - "+result);

        IngestStatus _result  =  objectMapper.readValue(result, IngestStatus.class);
        return _result;
    }

    public HttpResult sendJSON(String verb, String url, String json) throws  Exception {
        HttpResult result = basicHTTPRetriever.retrieveJSON(verb, url, json, null,null);
        return result;
    }
}
