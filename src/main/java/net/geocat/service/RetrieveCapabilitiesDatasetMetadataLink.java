package net.geocat.service;

import net.geocat.database.linkchecker.entities.CapabilitiesDatasetMetadataLink;
import net.geocat.database.linkchecker.entities.CapabilitiesRemoteDatasetMetadataDocument;
import net.geocat.database.linkchecker.entities.OperatesOnRemoteDatasetMetadataRecord;
import net.geocat.database.linkchecker.service.MetadataDocumentFactory;
import net.geocat.service.downloadhelpers.RetrievableSimpleLinkDownloader;
import net.geocat.xml.XmlDatasetMetadataDocument;
import net.geocat.xml.XmlDoc;
import net.geocat.xml.XmlDocumentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class RetrieveCapabilitiesDatasetMetadataLink {

    @Autowired
    RetrievableSimpleLinkDownloader retrievableSimpleLinkDownloader;

    @Autowired
    MetadataDocumentFactory metadataDocumentFactory;

    @Autowired
    XmlDocumentFactory xmlDocumentFactory;

    @Autowired
    LinkCheckBlobStorageService linkCheckBlobStorageService;

    public CapabilitiesDatasetMetadataLink process(CapabilitiesDatasetMetadataLink link) throws  Exception {
        link = (CapabilitiesDatasetMetadataLink) retrievableSimpleLinkDownloader.process(link);

        if (!link.getUrlFullyRead())
            return link;

        XmlDoc doc = xmlDocumentFactory.create(new String(link.getFullData()));

        if (!(doc instanceof XmlDatasetMetadataDocument))
            return link;

        XmlDatasetMetadataDocument xmlDatasetMetadataDocument = (XmlDatasetMetadataDocument) doc;
        String xmlStr = XmlDoc.writeXML(doc.getParsedXml());
        String sha2 = doc.computeSHA2(xmlStr);


        link.setSha2(sha2);
        linkCheckBlobStorageService.ensureBlobExists(xmlStr,sha2);

        CapabilitiesRemoteDatasetMetadataDocument capabilitiesRemoteDatasetMetadataDocument =  metadataDocumentFactory.createCapabilitiesRemoteDatasetMetadataDocument(link,xmlDatasetMetadataDocument);
        capabilitiesRemoteDatasetMetadataDocument.setSha2(sha2);

        link.setCapabilitiesRemoteDatasetMetadataDocument(capabilitiesRemoteDatasetMetadataDocument);

        return link;
    }
}
