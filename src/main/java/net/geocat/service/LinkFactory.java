package net.geocat.service;

import net.geocat.database.linkchecker.entities.Link;
import net.geocat.xml.XmlServiceRecordDoc;
import net.geocat.xml.helpers.OnlineResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LinkFactory {

    @Autowired
    CapabilitiesLinkFixer capabilitiesLinkFixer;

    public Link create(OnlineResource onlineResource,
                       XmlServiceRecordDoc doc,
                       String sha2,
                       String harvestId,
                       long endpointJobId,
                       String linkCheckJobId)
    throws Exception{
            Link result = new Link();

                result.setHarvestJobId(harvestId);
                result.setEndpointJobId(endpointJobId);
                result.setOriginatingServiceRecordSHA2(sha2);

                result.setOriginatingServiceRecordFileIdentifier(doc.getFileIdentifier());
                result.setOriginatingServiceRecordServiceType(doc.getServiceType());
                result.setRawLinkURL(onlineResource.getRawURL());
                result.setFixedLinkURL(capabilitiesLinkFixer.fix(onlineResource.getRawURL()));
                result.setLinkProtocol(onlineResource.getProtocol());
                result.setLinkOperationName(onlineResource.getOperationName());
                result.setLinkFunction(onlineResource.getFunction());
                result.setLinkCheckJobId(linkCheckJobId);

            return result;
    }
}
