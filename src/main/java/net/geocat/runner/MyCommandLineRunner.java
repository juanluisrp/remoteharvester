package net.geocat.runner;

import net.geocat.database.harvester.entities.EndpointJob;
import net.geocat.database.harvester.entities.MetadataRecord;
import net.geocat.database.harvester.repos.EndpointJobRepo;
import net.geocat.database.harvester.repos.MetadataRecordRepo;
import net.geocat.database.linkchecker.entities.Link;
import net.geocat.database.linkchecker.repos.LinkRepo;
import net.geocat.http.HttpResult;
import net.geocat.http.IHTTPRetriever;
import net.geocat.service.*;
import net.geocat.service.helper.NotServiceRecordException;
import net.geocat.xml.XmlDoc;
import net.geocat.xml.XmlDocumentFactory;
import net.geocat.xml.XmlServiceRecordDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.w3c.dom.NodeList;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    LinkRepo linkRepo;

    @Autowired
    MetadataRecordRepo metadataRecordRepo;

    @Autowired
    EndpointJobRepo endpointJobRepo;

    @Autowired
    ServiceDocLinkExtractor serviceDocLinkExtractor;

    @Autowired
    LinkProcessor_SimpleLinkRequest linkProcessor_simpleLinkRequest;

    @Autowired
    LinkProcessor_ProcessCapDoc linkProcessor_processCapDoc;

    @Autowired
    LinkProcessor_GetCapLinkedMetadata linkProcessor_getCapLinkedMetadata;

    @Override
    public void run(String...args) throws Exception {

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

        EndpointJob endpointJob = endpointJobRepo.findById(7L).get();
        List<MetadataRecord> records = metadataRecordRepo.findByEndpointJobId(endpointJob.getEndpointJobId());

       // records = records.subList(34,40);
       records = Arrays.asList( new MetadataRecord[]{records.get(66)});
      //records = Arrays.asList( new MetadataRecord[]{records.get(424),records.get(501),records.get(446),records.get(448)});
      //  records = Arrays.asList( new MetadataRecord[]{records.get(446),records.get(448)});
     // records = Arrays.asList( new MetadataRecord[]{records.get(33),records.get(37)});

        // MetadataRecord record = records.get(6);

        for(MetadataRecord record : records) {
            try {
                String sha2 = record.getSha2();
                String xml = blobStorageService.findXML(sha2);

                List<Link> links = serviceDocLinkExtractor.extractLinks(xml, sha2, endpointJob.getHarvestJobId(), endpointJob.getEndpointJobId());
                for (Link ll : links) {
                    try {
                        ll = linkProcessor_simpleLinkRequest.process(ll);

                        ll = linkProcessor_processCapDoc.process(ll);

                        ll = linkProcessor_getCapLinkedMetadata.process(ll);
                        linkRepo.save(ll);
                        int t = 0;
                    }
                    catch(Exception e)
                    {
                        int tt=0;
                    }
                }
            }
            catch(NotServiceRecordException notServiceRecordException){
                int utt=0;
            }
            catch(Exception e){
                int t=0;
            }
        }
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

