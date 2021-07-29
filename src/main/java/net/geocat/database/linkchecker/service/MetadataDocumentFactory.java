package net.geocat.database.linkchecker.service;

import net.geocat.database.linkchecker.entities.*;
import net.geocat.database.linkchecker.entities.helper.DatasetMetadataRecord;
import net.geocat.database.linkchecker.entities.helper.MetadataRecord;
import net.geocat.database.linkchecker.entities.helper.ServiceMetadataRecord;
import net.geocat.service.ServiceDocLinkExtractor;
import net.geocat.xml.XmlDatasetMetadataDocument;
import net.geocat.xml.XmlMetadataDocument;
import net.geocat.xml.XmlServiceRecordDoc;
import net.geocat.xml.helpers.OnlineResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
public class MetadataDocumentFactory {

    @Autowired
    ServiceDocLinkExtractor serviceDocLinkExtractor;

    @Autowired
    ServiceDocumentLinkService serviceDocumentLinkService;

    @Autowired
    OperatesOnLinkService operatesOnLinkService;

    @Autowired
    ServiceMetadataRecordService serviceMetadataRecordService;

    @Autowired
    RemoteServiceMetadataRecordService remoteServiceMetadataRecordService;

    @Autowired
    RemoteDatasetMetadataRecordService remoteDatasetMetadataRecordService;

    @Autowired
    CapabilitiesDatasetMetadataLinkService capabilitiesDatasetMetadataLinkService;



    public void augment(MetadataRecord metadataRecord, XmlMetadataDocument xml){
        metadataRecord.setMetadataRecordType(xml.getMetadataDocumentType());
        metadataRecord.setFileIdentifier(xml.getFileIdentifier());
    }

    public void augment(DatasetMetadataRecord datasetMetadataRecord, XmlDatasetMetadataDocument xml){
        augment( (MetadataRecord) datasetMetadataRecord, (XmlMetadataDocument)xml);
        datasetMetadataRecord.setDatasetIdentifier(xml.getDatasetIdentifier());
    }

    public void augment(ServiceMetadataRecord serviceMetadataRecord, XmlServiceRecordDoc xml) throws Exception {
        augment( (MetadataRecord) serviceMetadataRecord, (XmlMetadataDocument)xml);

        serviceMetadataRecord.setMetadataServiceType(xml.getServiceType());
        List<OnlineResource> links = serviceDocLinkExtractor.extractOnlineResource(xml);

        List<ServiceDocumentLink> links2 = links.stream().map(x->serviceDocumentLinkService.create(serviceMetadataRecord,x)).collect(Collectors.toList());
        serviceMetadataRecord.setServiceDocumentLinks(links2);

        List<OperatesOnLink> operatesOnLinks = xml.getOperatesOns().stream()
                .map(x->operatesOnLinkService.create(serviceMetadataRecord,x))
                .collect(Collectors.toList());

        serviceMetadataRecord.setOperatesOnLinks(operatesOnLinks);
    }

    public LocalServiceMetadataRecord createLocalServiceMetadataRecord(XmlServiceRecordDoc doc,
                                                                       Long underlyingHarvestMetadataRecordId,
                                                                       String linkCheckJobId,
                                                                       String sha2) throws Exception {
        LocalServiceMetadataRecord result = serviceMetadataRecordService.create(doc,underlyingHarvestMetadataRecordId,linkCheckJobId,sha2);
        augment(result,doc);
        return result;
    }

    public RemoteServiceMetadataRecord createRemoteServiceMetadataRecord(RemoteServiceMetadataRecordLink link,XmlServiceRecordDoc xmlServiceRecordDoc,String sha2) throws Exception {
        RemoteServiceMetadataRecord result = remoteServiceMetadataRecordService.create(link);
        augment(result,xmlServiceRecordDoc);
        link.setRemoteServiceMetadataRecord(result);
        result.setSha2(sha2);
        return result;
    }

    public OperatesOnRemoteDatasetMetadataRecord createRemoteDatasetMetadataRecord(OperatesOnLink link, XmlDatasetMetadataDocument doc){
        OperatesOnRemoteDatasetMetadataRecord result = remoteDatasetMetadataRecordService.createRemoteDatasetMetadataRecord(link);
        augment(result,doc);
        result.setOperatesOnLink(link);
        return result;
    }

    public CapabilitiesRemoteDatasetMetadataDocument createCapabilitiesRemoteDatasetMetadataDocument(
            CapabilitiesDatasetMetadataLink link, XmlDatasetMetadataDocument doc){
        CapabilitiesRemoteDatasetMetadataDocument result = remoteDatasetMetadataRecordService.createCapabilitiesRemoteDatasetMetadataDocument(link);
        augment(result,doc);
        result.setCapabilitiesDatasetMetadataLink(link);
        return result;
    }

}
