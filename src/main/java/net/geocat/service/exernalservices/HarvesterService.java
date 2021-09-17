package net.geocat.service.exernalservices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.geocat.database.linkchecker.entities.helper.HttpResult;
import net.geocat.http.BasicHTTPRetriever;
import net.geocat.http.ExceptionWithCookies;
import net.geocat.http.RedirectException;
import net.geocat.model.HarvestStartResponse;
import net.geocat.model.HarvesterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Scope("prototype")
public class HarvesterService {

    @Autowired
    BasicHTTPRetriever basicHTTPRetriever;

    ObjectMapper objectMapper = new ObjectMapper();

    @Value("${harvester.url}")
    String harvesterAPIURL;

    // call the harvest remote service and return the processID
    public HarvestStartResponse startHarvest(HarvesterConfig harvestConfig) throws  Exception {

        String url = harvesterAPIURL+"/startHarvest";
        String requestJSON = objectMapper.writeValueAsString(harvestConfig);

        String response = sendJSON("POST",url, requestJSON);

        HarvestStartResponse result = objectMapper.readValue(response, HarvestStartResponse.class);

        return result;
    }

    public String sendJSON(String verb, String url, String json) throws  Exception {
        HttpResult result = basicHTTPRetriever.retrieveJSON(verb, url, json, null,null);
        return new String(result.getData());
    }
}
