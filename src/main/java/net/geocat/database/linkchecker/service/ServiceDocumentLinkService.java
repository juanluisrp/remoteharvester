package net.geocat.database.linkchecker.service;

import net.geocat.database.linkchecker.entities.ServiceDocumentLink;
import net.geocat.database.linkchecker.entities.LocalServiceMetadataRecord;
import net.geocat.database.linkchecker.entities.helper.ServiceMetadataRecord;
import net.geocat.xml.helpers.OnlineResource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ServiceDocumentLinkService {



    public ServiceDocumentLink create(ServiceMetadataRecord localServiceMetadataRecord, OnlineResource onlineResource){
        ServiceDocumentLink result = new ServiceDocumentLink();

            result.setServiceMetadataRecord(localServiceMetadataRecord);
            result.setFunction(onlineResource.getFunction());
            result.setOperationName(onlineResource.getOperationName());
            result.setRawURL(onlineResource.getRawURL());
            result.setProtocol(onlineResource.getProtocol());

        return result;
    }
}
