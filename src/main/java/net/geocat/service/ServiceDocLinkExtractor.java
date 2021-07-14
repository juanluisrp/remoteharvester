package net.geocat.service;


import net.geocat.database.linkchecker.entities.Link;
import net.geocat.service.helper.NotServiceRecordException;
import net.geocat.xml.XmlDoc;
import net.geocat.xml.XmlDocumentFactory;
import net.geocat.xml.XmlServiceRecordDoc;
import net.geocat.xml.helpers.OnlineResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
public class ServiceDocLinkExtractor {

    @Autowired
    XmlDocumentFactory xmlDocumentFactory;

    @Autowired
    LinkFactory linkFactory;

    public List<Link> extractLinks(String xml,
                                   String sha2,
                                   String harvestId,
                                   long endpointId,
                                   String linkCheckJobId) throws Exception {
        XmlDoc doc = xmlDocumentFactory.create(xml);
        if (doc instanceof XmlServiceRecordDoc)
            return extractLinks((XmlServiceRecordDoc)doc,sha2,harvestId,endpointId,linkCheckJobId);
        throw new NotServiceRecordException("trying to extract links from a non-service record");
     }

    public List<Link> extractLinks(XmlServiceRecordDoc xml,
                                   String sha2,
                                   String harvestId,
                                   long endpointId,
                                   String linkCheckJobId) throws Exception{
        List<Link> result = new ArrayList<>();

        List<OnlineResource> docLinks = removeDuplicates(xml.getConnectPoints(),xml.getTransferOptions());

        for(OnlineResource onlineResource: docLinks){
            Link link = linkFactory.create(onlineResource,xml,sha2, harvestId, endpointId,linkCheckJobId);
            result.add(link);
        }
        return result;
    }

    public boolean badURL(String rawUrl)  {
       if  ((rawUrl == null) || (rawUrl.isEmpty()))
           return true;
       try{
           URL url = new URL(rawUrl);
           String protocol = url.getProtocol().toLowerCase();
           if (protocol == null)
               return true;
           if (!protocol.equals("http") && (!protocol.equals("https")))
               return true;
           URI uri = new URL(rawUrl).toURI(); //should throw if invalid
           return false;
       }
       catch (Exception e)
       {
           return false;
       }

    }

    //we can preference of connectPoints since they might have an operation's name
    // note - could have duplicates inside the individual lists, but more likely between them
    public List<OnlineResource> removeDuplicates(List<OnlineResource> connectPoints,
                                                 List<OnlineResource> transferOptions) throws Exception {
        List<OnlineResource> result = new ArrayList<>();
        for(OnlineResource onlineResource : connectPoints){
            if ( badURL(onlineResource.getRawURL())  )
                continue;
            if (!inList(onlineResource,result))
                result.add(onlineResource);
        }
        for(OnlineResource onlineResource : transferOptions){
            if ( badURL(onlineResource.getRawURL())  )
                continue;
             if (!inList(onlineResource,result))
                result.add(onlineResource);
        }
        return result;
    }

    public boolean inList(OnlineResource onlineResource, List<OnlineResource> list){
        for (OnlineResource or : list){
            if (onlineResource.getRawURL().equals( or.getRawURL()))
                return true;
        }
        return false;
    }

}
