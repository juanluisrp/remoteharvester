package net.geocat.database.linkchecker.service;

import net.geocat.database.linkchecker.entities.CapabilitiesDatasetMetadataLink;
import net.geocat.database.linkchecker.entities.CapabilitiesDocument;
import net.geocat.database.linkchecker.entities.RemoteServiceMetadataRecordLink;
import net.geocat.database.linkchecker.entities.ServiceDocumentLink;
import net.geocat.database.linkchecker.entities2.IndicatorStatus;
import net.geocat.service.BlobStorageService;
import net.geocat.service.LinkCheckBlobStorageService;
import net.geocat.xml.XmlCapabilitiesDocument;
import net.geocat.xml.XmlDoc;
import net.geocat.xml.XmlDocumentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class CapabilitiesDocumentService {

    @Autowired
    BlobStorageService blobStorageService;

    @Autowired
    XmlDocumentFactory xmlDocumentFactory;

    @Autowired
    LinkCheckBlobStorageService linkCheckBlobStorageService;

    @Autowired
    RemoteServiceMetadataRecordService remoteServiceMetadataRecordService;

    @Autowired
    CapabilitiesDatasetMetadataLinkService capabilitiesDatasetMetadataLinkService;

    public CapabilitiesDocument create(ServiceDocumentLink link ) throws Exception {
        String xmlStr = new String(link.getFullData());
        XmlCapabilitiesDocument xml = (XmlCapabilitiesDocument) xmlDocumentFactory.create(xmlStr);

        xmlStr =  XmlDoc.writeXML(xml.getParsedXml());
        String sha2= xml.computeSHA2(xmlStr);

        linkCheckBlobStorageService.ensureBlobExists(xmlStr,sha2); //write

        CapabilitiesDocument doc = new CapabilitiesDocument();
        doc.setSha2(sha2);
        doc.setServiceDocumentLink(link);
        doc.setCapabilitiesDocumentType(xml.getCapabilitiesType());



        if (xml.isHasExtendedCapabilities()) {
            doc.setIndicator_HasExtendedCapabilities(IndicatorStatus.PASS);
        }
        else {
            doc.setIndicator_HasExtendedCapabilities(IndicatorStatus.FAIL);
            return doc;
        }

        String metadataUrl = xml.getMetadataUrlRaw();
        if ( (metadataUrl == null) || (metadataUrl.isEmpty()) ) {
            doc.setIndicator_HasServiceMetadataLink(IndicatorStatus.FAIL);
            return doc;
        }

        doc.setIndicator_HasServiceMetadataLink(IndicatorStatus.PASS);

        RemoteServiceMetadataRecordLink remoteServiceMetadataRecordLink = remoteServiceMetadataRecordService.create(doc,metadataUrl);
        doc.setRemoteServiceMetadataRecord(remoteServiceMetadataRecordLink);


        List<CapabilitiesDatasetMetadataLink> dslinks= capabilitiesDatasetMetadataLinkService.createCapabilitiesDatasetMetadataLinks(doc,xml);
        doc.setCapabilitiesDatasetMetadataLinkList(dslinks);

        return doc;
    }


}
