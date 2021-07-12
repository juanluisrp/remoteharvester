package net.geocat.service;

import net.geocat.database.linkchecker.entities.IndicatorStatus;
import net.geocat.database.linkchecker.entities.Link;
import net.geocat.xml.XmlCapabilitiesDocument;
import net.geocat.xml.XmlDoc;
import net.geocat.xml.XmlDocumentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class LinkProcessor_ProcessCapDoc implements ILinkProcessor {

     @Autowired
     BlobStorageService blobStorageService;

     @Autowired
     XmlDocumentFactory xmlDocumentFactory;

    @Override
    public Link process(Link link) throws Exception {

        String sha2 = link.getLinkContentSHA2();
        if ( (sha2 == null) || (sha2.isEmpty()) )
            return link;

        String doc = blobStorageService.findXML(sha2);
        if ( (doc == null) || (doc.isEmpty()) )
            throw new Exception("couldnt load cap document from blob storage!");

        XmlCapabilitiesDocument xml = (XmlCapabilitiesDocument) xmlDocumentFactory.create(doc);

        if (xml.isHasExtendedCapabilities()) {
            link.setIndicator_HasExtendedCapabilities(IndicatorStatus.PASS);
        }
        else {
            link.setIndicator_HasExtendedCapabilities(IndicatorStatus.FAIL);
            return link;
        }

        String metadataUrl = xml.getMetadataUrlRaw();
        if ( (metadataUrl == null) || (metadataUrl.isEmpty()) ) {
            link.setIndicator_HasServiceMetadataLink(IndicatorStatus.FAIL);
            return link;
        }

        link.setIndicator_HasServiceMetadataLink(IndicatorStatus.PASS);
        link.setServiceMetadataLinkURL(metadataUrl);

        return link;
    }
}
