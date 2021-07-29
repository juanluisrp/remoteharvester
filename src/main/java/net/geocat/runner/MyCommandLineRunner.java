package net.geocat.runner;

import net.geocat.database.harvester.entities.MetadataRecord;
import net.geocat.database.harvester.repos.MetadataRecordRepo;
import net.geocat.database.linkchecker.entities.*;
import net.geocat.database.linkchecker.repos.*;
import net.geocat.database.linkchecker.service.MetadataDocumentFactory;
import net.geocat.database.linkchecker.service.OperatesOnLinkService;
import net.geocat.database.linkchecker.service.ServiceDocumentLinkService;
import net.geocat.database.linkchecker.service.ServiceMetadataRecordService;
import net.geocat.http.IHTTPRetriever;
import net.geocat.service.*;
import net.geocat.service.downloadhelpers.RetrievableSimpleLinkDownloader;
import net.geocat.xml.XmlDocumentFactory;
import net.geocat.xml.XmlServiceRecordDoc;
import net.geocat.xml.helpers.OnlineResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MyCommandLineRunner implements CommandLineRunner {
    private static final Logger logger =    LoggerFactory.getLogger(MyCommandLineRunner.class);


    @Autowired
    BlobStorageService blobStorageService;

    @Autowired
    XmlDocumentFactory xmlDocumentFactory;

    @Autowired
    @Qualifier("cookieAttachingRetriever")
    IHTTPRetriever retriever;

//    @Autowired
//    LinkRepo linkRepo;

    @Autowired
    MetadataRecordRepo metadataRecordRepo;
//
//    @Autowired
//    EndpointJobRepo endpointJobRepo;

    @Autowired
    ServiceDocLinkExtractor serviceDocLinkExtractor;

    @Autowired
    LinkProcessor_SimpleLinkRequest linkProcessor_simpleLinkRequest;

    @Autowired
    LinkProcessor_ProcessCapDoc linkProcessor_processCapDoc;

    @Autowired
    LinkProcessor_GetCapLinkedMetadata linkProcessor_getCapLinkedMetadata;

    @Autowired
    ServiceMetadataRecordService serviceMetadataRecordService;

    @Autowired
    ServiceDocumentLinkService serviceDocumentLinkService;

    @Autowired
    LocalServiceMetadataRecordRepo localServiceMetadataRecordRepo;

    @Autowired
    OperatesOnLinkService operatesOnLinkService;

    @Autowired
    RetrieveServiceDocumentLink retrieveServiceDocumentLink;

    @Autowired
    ServiceDocumentLinkRepo serviceDocumentLinkRepo;

    @Autowired
    RemoteServiceMetadataRecordLinkRetriever remoteServiceMetadataRecordLinkRetriever;

    @Autowired
    MetadataDocumentFactory metadataDocumentFactory;

    @Autowired
    RetrieveOperatesOnLink retrieveOperatesOnLink;

    @Autowired
    RemoteServiceMetadataRecordLinkRepo remoteServiceMetadataRecordLinkRepo;

    @Autowired
    OperatesOnLinkRepo operatesOnLinkRepo;

    @Autowired
    CapabilitiesDatasetMetadataLinkRepo capabilitiesDatasetMetadataLinkRepo;

    @Autowired
    RetrieveCapabilitiesDatasetMetadataLink retrieveCapabilitiesDatasetMetadataLink;

    @Override
    public void run(String...args) throws Exception {
        String sha2_wmts = "49D36EFBA4B7A2541D31E44FC9557A8BEB0A7E275F1F22AAA00EEE953C41FF70";

        String sha2_atom = "76ABB39998E3EF07307059CC3B703A3959A48447DB98203FBF686856E0C6D4E3";

        String sha2_wms = "CB17C3851790A134F62C391DD9FFCC51052BF3451ADDE1ED07CA79430E4C1281";
        String sha2_wfs = "9ADC3010703B006F47870F7572D4C796DDF5542ADB3B4DF923015E1095FA3A89";

        String sha2_wms2 = "5E189A51157C500E3D870653B976706D7B97EF23B9220DF2DC99B8592693845E";
        String sha2_wms3 = "F42EA03157107422350861C3EECC260BF7CF06A01D206113F85D2298BC24123F";

        String sha2 = "0E75379A7CA9984B6AEFDEBCFA5AC92A0FE1D3BB47F2D1919BC318F49A7AB91C";

         MetadataRecord metadataRecord = metadataRecordRepo.findBySha2(sha2).get(0);
        String xml = blobStorageService.findXML(sha2);


        XmlServiceRecordDoc doc = (XmlServiceRecordDoc) xmlDocumentFactory.create(xml);

        LocalServiceMetadataRecord localServiceMetadataRecord =
                metadataDocumentFactory.createLocalServiceMetadataRecord(doc,metadataRecord.getMetadataRecordId(),"TESTCASE",sha2);

//        List<OnlineResource> links = serviceDocLinkExtractor.extractOnlineResource(doc);
//
//        List<ServiceDocumentLink> links2 = links.stream().map(x->serviceDocumentLinkService.create(localServiceMetadataRecord,x)).collect(Collectors.toList());
//        localServiceMetadataRecord.setServiceDocumentLinks(links2);
//
//        List<OperatesOnLink> operatesOnLinks = doc.getOperatesOns().stream()
//                                .map(x->operatesOnLinkService.create(localServiceMetadataRecord,x))
//                .collect(Collectors.toList());
//
    //    localServiceMetadataRecord.setOperatesOnLinks(operatesOnLinks);

        LocalServiceMetadataRecord sm11 = localServiceMetadataRecordRepo.findById(1L).get();
        CapabilitiesDocument cd = sm11.getServiceDocumentLinks().get(1).getCapabilitiesDocument();
        RemoteServiceMetadataRecordLink cd_rsmrl = cd.getRemoteServiceMetadataRecord();
        String ss = cd_rsmrl.toString();
        List<OperatesOnLink> ll =  sm11.getOperatesOnLinks();
        int t6t = ll.size();

        LocalServiceMetadataRecord smr =   localServiceMetadataRecordRepo.save(localServiceMetadataRecord);

        List<LocalServiceMetadataRecord> list = Arrays.asList(new LocalServiceMetadataRecord[] {smr} );
        for(LocalServiceMetadataRecord record : list){

            List<RemoteServiceMetadataRecordLink> remoteLinks = new ArrayList<>();
            List<CapabilitiesDatasetMetadataLink> capabilitiesDatasetMetadataLinkList = new ArrayList<>();

            //process servicedocumentlinks
            for (ServiceDocumentLink link: record.getServiceDocumentLinks()){
                link = serviceDocumentLinkRepo.findById(link.getServiceMetadataLinkId()).get();// make sure we re-load
                ServiceDocumentLink link2 = retrieveServiceDocumentLink.process(link);
                link2 = serviceDocumentLinkRepo.save(link2); //re-save
                if (link2.getCapabilitiesDocument() !=null) {
                    CapabilitiesDocument capabilitiesDocument = link2.getCapabilitiesDocument();
                    RemoteServiceMetadataRecordLink rsmrl = capabilitiesDocument.getRemoteServiceMetadataRecord();
                    if (rsmrl != null) {
                        remoteLinks.add(rsmrl);
                    }
                    for(CapabilitiesDatasetMetadataLink capabilitiesDatasetMetadataLink: capabilitiesDocument.getCapabilitiesDatasetMetadataLinkList()){
                        capabilitiesDatasetMetadataLinkList.add(capabilitiesDatasetMetadataLink);
                    }
                }
                int t=0;
            }

            //process resulting remoteMetadataRecordLinks
            for (RemoteServiceMetadataRecordLink link: remoteLinks){
                RemoteServiceMetadataRecordLink link2 = remoteServiceMetadataRecordLinkRepo.findById(link.getRemoteServiceMetadataRecordLinkId()).get();
             //   CapabilitiesDocument cd = link2.getCapabilitiesDocument();
                RemoteServiceMetadataRecordLink rsmrl = remoteServiceMetadataRecordLinkRetriever.process(link2);
                RemoteServiceMetadataRecordLink rsmrl2 = remoteServiceMetadataRecordLinkRepo.save(rsmrl);
                int t=0;
            }

            //process resulting capabilities doc dataset links
            for (CapabilitiesDatasetMetadataLink link: capabilitiesDatasetMetadataLinkList){
                CapabilitiesDatasetMetadataLink link2 = capabilitiesDatasetMetadataLinkRepo.findById(link.getCapabilitiesDatasetMetadataLinkId()).get();
                //   CapabilitiesDocument cd = link2.getCapabilitiesDocument();
                CapabilitiesDatasetMetadataLink cdml = retrieveCapabilitiesDatasetMetadataLink.process(link2);
                CapabilitiesDatasetMetadataLink cdml2 = capabilitiesDatasetMetadataLinkRepo.save(cdml);
                int t=0;
            }


            for(OperatesOnLink operatesOnLink : record.getOperatesOnLinks()) {
                operatesOnLink = operatesOnLinkRepo.findById(operatesOnLink.getOperatesOnLinkId()).get(); //reload
                OperatesOnLink operatesOnLink2 = retrieveOperatesOnLink.process(operatesOnLink);
                operatesOnLink2 = operatesOnLinkRepo.save(operatesOnLink2);
                int tt=0;
            }
        }

        int t=0;
    }

        public void run2(String...args) throws Exception {

        logger.debug("hi there");
//       HttpResult r= retriever.retrieveXML("GET","https://google.com/badurl", null, null,null);
//    int ut=0;

        //Optional<Link> l = linkRepo.findById(1L);
//
//        String sha2_wms = "65B3FFA90B5B277B34C7206D0283B7CD1FE89AE473998A566239992C4A59A417";
//        String sha2_wfs = "8759C3E02840BC5DE3F81B23F4AF1D124821384252AC018A0BDEB0386B76E663";
//        String sha2_atom = "546108E2F8D91EEE3E0684736F1469953EF8F71A73A63849E19B84D027704857";
//        String sha2_wmts = "49D36EFBA4B7A2541D31E44FC9557A8BEB0A7E275F1F22AAA00EEE953C41FF70";
//
//        String sha2 = sha2_wms;
//
//        MetadataRecord metadataRecord = metadataRecordRepo.findBySha2(sha2).get(0);
//        EndpointJob endpointJob = endpointJobRepo.findById(metadataRecord.getEndpointJobId()).get();
//        String xml = blobStorageService.findXML(sha2);

//        EndpointJob endpointJob = endpointJobRepo.findById(40L).get();
//        List<MetadataRecord> records = metadataRecordRepo.findByEndpointJobId(endpointJob.getEndpointJobId());

       // records = records.subList(34,40);
     //  records = Arrays.asList( new MetadataRecord[]{records.get(66)});
      //records = Arrays.asList( new MetadataRecord[]{records.get(424),records.get(501),records.get(446),records.get(448)});
      //  records = Arrays.asList( new MetadataRecord[]{records.get(446),records.get(448)});
     // records = Arrays.asList( new MetadataRecord[]{records.get(33),records.get(37)});

        // MetadataRecord record = records.get(6);

//        for(MetadataRecord record : records) {
//            try {
//                String sha2 = record.getSha2();
//                String xml = blobStorageService.findXML(sha2);
//
//                List<Link> links = serviceDocLinkExtractor.extractLinks(
//                        xml, sha2, endpointJob.getHarvestJobId(), endpointJob.getEndpointJobId(),"testcase");
//                for (Link ll : links) {
//                    try {
//                        ll = linkProcessor_simpleLinkRequest.process(ll);
//
//                        ll = linkProcessor_processCapDoc.process(ll);
//
//                        ll = linkProcessor_getCapLinkedMetadata.process(ll);
//                     //   linkRepo.save(ll);
//                        int t = 0;
//                    }
//                    catch(Exception e)
//                    {
//                        int tt=0;
//                    }
//                }
//            }
//            catch(NotServiceRecordException notServiceRecordException){
//                int utt=0;
//            }
//            catch(Exception e){
//                int t=0;
//            }
//        }
//        XmlDoc xmlDoc = xmlDocumentFactory.create(xml);
//
//        if (xmlDoc instanceof XmlServiceRecordDoc)
//        {
//            XmlServiceRecordDoc xmlServiceRecord = (XmlServiceRecordDoc) xmlDoc;
//            String capURL = xmlServiceRecord.getConnectPoints().get(0).getRawURL();
//            String capXML = new String(retriever.retrieveXML("GET",capURL,null,null,null));
//            XmlDoc capXMLDoc = xmlDocumentFactory.create(capXML);
//            int t=0;
//        }

    }
}

