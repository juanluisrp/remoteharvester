package net.geocat.service;

import net.geocat.database.linkchecker.entities.RemoteServiceMetadataRecord;
import net.geocat.database.linkchecker.entities.RemoteServiceMetadataRecordLink;
import net.geocat.database.linkchecker.service.MetadataDocumentFactory;
import net.geocat.database.linkchecker.service.RemoteServiceMetadataRecordService;
import net.geocat.service.downloadhelpers.RetrievableSimpleLinkDownloader;
import net.geocat.xml.XmlDoc;
import net.geocat.xml.XmlDocumentFactory;
import net.geocat.xml.XmlServiceRecordDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class RemoteServiceMetadataRecordLinkRetriever {

    @Autowired
    RetrievableSimpleLinkDownloader retrievableSimpleLinkDownloader;

    @Autowired
    XmlDocumentFactory xmlDocumentFactory;

    @Autowired
    LinkCheckBlobStorageService linkCheckBlobStorageService;

//    @Autowired
//    RemoteServiceMetadataRecordService remoteServiceMetadataRecordService;

    @Autowired
    MetadataDocumentFactory metadataDocumentFactory;

    public RemoteServiceMetadataRecordLink process(RemoteServiceMetadataRecordLink link) throws Exception {
        link = (RemoteServiceMetadataRecordLink) retrievableSimpleLinkDownloader.process(link);

        if (!link.getUrlFullyRead())
            return link;

        String xmlStr = new String(link.getFullData());
        XmlDoc xmlDoc = xmlDocumentFactory.create(xmlStr);

        if (!(xmlDoc instanceof XmlServiceRecordDoc))
            return link;
        XmlServiceRecordDoc xmlServiceRecordDoc = (XmlServiceRecordDoc) xmlDoc;

        xmlStr = XmlDoc.writeXML(xmlDoc.getParsedXml());
        String sha2 = xmlDoc.computeSHA2(xmlStr);


        link.setSha2(sha2);
        linkCheckBlobStorageService.ensureBlobExists(xmlStr,sha2);

        RemoteServiceMetadataRecord remoteServiceMetadataRecord =
                metadataDocumentFactory.createRemoteServiceMetadataRecord(link, xmlServiceRecordDoc,sha2);


        return link;

    }
}
