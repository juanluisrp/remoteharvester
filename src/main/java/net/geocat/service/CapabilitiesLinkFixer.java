package net.geocat.service;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

@Component
public class CapabilitiesLinkFixer implements ILinkFixer{
    @Override
    public String fix(String link) throws Exception {

        String requestParam = findQueryParmName(link,"request");
        if (requestParam == null)
            return link;

        URIBuilder uriBuilder = new URIBuilder(link);
        uriBuilder.setParameter(requestParam,"GetCapabilities");
        return uriBuilder.build().toString();
    }

    public static String findQueryParmName(String link, String name) throws  Exception {
        name = name.toLowerCase();
        URIBuilder uriBuilder = new URIBuilder(link);
        for(NameValuePair param : uriBuilder.getQueryParams()){
            if (param.getName().toLowerCase().equals(name))
                return param.getName();
        }
        return null;
    }
}
