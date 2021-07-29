package net.geocat.database.linkchecker.service;

import net.geocat.database.linkchecker.entities.CapabilitiesDocument;
import net.geocat.database.linkchecker.entities.RemoteServiceMetadataRecord;
import net.geocat.database.linkchecker.entities.RemoteServiceMetadataRecordLink;
import net.geocat.xml.XmlServiceRecordDoc;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class RemoteServiceMetadataRecordService {

    public RemoteServiceMetadataRecordLink create(CapabilitiesDocument capabilitiesDocument, String rawURL){
        RemoteServiceMetadataRecordLink result = new RemoteServiceMetadataRecordLink();
        result.setRawURL(rawURL);
        result.setCapabilitiesDocument(capabilitiesDocument);
        return result;
    }

    public RemoteServiceMetadataRecord create(RemoteServiceMetadataRecordLink link ){
        RemoteServiceMetadataRecord result = new RemoteServiceMetadataRecord();
        result.setRemoteServiceMetadataRecordLink(link);
        link.setRemoteServiceMetadataRecord(result);

        return result;
    }
}
