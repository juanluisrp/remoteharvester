package net.geocat.database.linkchecker.service;

import net.geocat.database.linkchecker.entities.CapabilitiesDatasetMetadataLink;
import net.geocat.database.linkchecker.entities.CapabilitiesDocument;
import net.geocat.service.capabilities.DatasetLink;
import net.geocat.xml.XmlCapabilitiesDocument;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class CapabilitiesDatasetMetadataLinkService {


    public List<CapabilitiesDatasetMetadataLink> createCapabilitiesDatasetMetadataLinks (CapabilitiesDocument cap, XmlCapabilitiesDocument doc){
        List<CapabilitiesDatasetMetadataLink> result = new ArrayList<>();
        for(DatasetLink link :doc.getDatasetLinksList()){

            CapabilitiesDatasetMetadataLink item = new CapabilitiesDatasetMetadataLink();

            item.setRawURL(link.getRawUrl());
            item.setIdentity(link.getIdentifier());

            item.setCapabilitiesDocument(cap);
            result.add(item);
        }
        return result;
    }
}
