package net.geocat.database.linkchecker.service;

import net.geocat.database.linkchecker.entities.OperatesOnLink;
import net.geocat.database.linkchecker.entities.LocalServiceMetadataRecord;
import net.geocat.database.linkchecker.entities.helper.ServiceMetadataRecord;
import net.geocat.xml.helpers.OperatesOn;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class OperatesOnLinkService {

    public OperatesOnLink create(ServiceMetadataRecord serviceMetadataRecord, OperatesOn operatesOn){
        OperatesOnLink result = new OperatesOnLink();

        result.setRawURL(operatesOn.getRawUrl());
        result.setUuidref(operatesOn.getUuidref());
        result.setServiceMetadataRecord(serviceMetadataRecord);

        return result;
    }

}
