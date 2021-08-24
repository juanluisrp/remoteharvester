/*
 *  =============================================================================
 *  ===  Copyright (C) 2021 Food and Agriculture Organization of the
 *  ===  United Nations (FAO-UN), United Nations World Food Programme (WFP)
 *  ===  and United Nations Environment Programme (UNEP)
 *  ===
 *  ===  This program is free software; you can redistribute it and/or modify
 *  ===  it under the terms of the GNU General Public License as published by
 *  ===  the Free Software Foundation; either version 2 of the License, or (at
 *  ===  your option) any later version.
 *  ===
 *  ===  This program is distributed in the hope that it will be useful, but
 *  ===  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  ===  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  ===  General Public License for more details.
 *  ===
 *  ===  You should have received a copy of the GNU General Public License
 *  ===  along with this program; if not, write to the Free Software
 *  ===  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 *  ===
 *  ===  Contact: Jeroen Ticheler - FAO - Viale delle Terme di Caracalla 2,
 *  ===  Rome - Italy. email: geonetwork@osgeo.org
 *  ===
 *  ===  Development of this program was financed by the European Union within
 *  ===  Service Contract NUMBER – 941143 – IPR – 2021 with subject matter
 *  ===  "Facilitating a sustainable evolution and maintenance of the INSPIRE
 *  ===  Geoportal", performed in the period 2021-2023.
 *  ===
 *  ===  Contact: JRC Unit B.6 Digital Economy, Via Enrico Fermi 2749,
 *  ===  21027 Ispra, Italy. email: JRC-INSPIRE-SUPPORT@ec.europa.eu
 *  ==============================================================================
 */

package net.geocat.runner;

import net.geocat.database.harvester.entities.MetadataRecord;
import net.geocat.database.harvester.repos.MetadataRecordRepo;
import net.geocat.database.linkchecker.entities.*;
import net.geocat.database.linkchecker.entities.helper.ServiceMetadataRecord;
import net.geocat.database.linkchecker.entities.helper.StatusQueryItem;
import net.geocat.database.linkchecker.entities2.IndicatorStatus;
import net.geocat.database.linkchecker.repos.*;
import net.geocat.database.linkchecker.service.*;
import net.geocat.eventprocessor.processors.processlinks.postprocessing.*;
import net.geocat.http.IHTTPRetriever;
import net.geocat.service.*;
import net.geocat.xml.XmlCapabilitiesDocument;
import net.geocat.xml.XmlDoc;
import net.geocat.xml.XmlDocumentFactory;
import net.geocat.xml.XmlServiceRecordDoc;
import net.geocat.xml.helpers.CapabilitiesType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.jws.Oneway;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MyCommandLineRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(MyCommandLineRunner.class);


    @Autowired
    BlobStorageService blobStorageService;

    @Autowired
    XmlDocumentFactory xmlDocumentFactory;

    @Autowired
    LinkCheckBlobStorageRepo linkCheckBlobStorageRepo;

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


    @Autowired
    HumanReadableServiceMetadata humanReadableServiceMetadata;

    @Autowired
    CapabilitiesDocumentRepo capabilitiesDocumentRepo;

    @Autowired
    LocalDatasetMetadataRecordRepo localDatasetMetadataRecordRepo;

    @Autowired
    CapabilitiesResolvesIndicators capabilitiesResolvesIndicators;

    @Autowired
    CapabilitiesServiceLinkIndicators capabilitiesServiceLinkIndicators;

    @Autowired
    CapabilitiesServiceMatchesLocalServiceIndicators capabilitiesServiceMatchesLocalServiceIndicators;

    @Autowired
    CapabilitiesDatasetLinksResolveIndicators capabilitiesDatasetLinksResolveIndicators;

    @Autowired
    ServiceOperatesOnIndicators serviceOperatesOnIndicators;

    @Autowired
    CapabilitiesDatasetMetadataLinkService capabilitiesDatasetMetadataLinkService;




    @Override
    public void run(String... args) throws Exception {
        // run3(args);
      //  LocalServiceMetadataRecord sm11 = localServiceMetadataRecordRepo.findById(12248L).get();
        try {
    run11(args);
      run12(args);
        }
        catch(Exception e){
            int t=0;
        }
        logger.debug("DONE!");
    }


    public CapabilitiesDocument createCap(XmlCapabilitiesDocument xml) throws Exception {
        String sha2 ="test case";
        CapabilitiesDocument doc = new CapabilitiesDocument();
        doc.setSha2(sha2);

       // doc.setParent(link);
        doc.setCapabilitiesDocumentType(xml.getCapabilitiesType());


        if (xml.isHasExtendedCapabilities()) {
            doc.setIndicator_HasExtendedCapabilities(IndicatorStatus.PASS);
        } else {
            doc.setIndicator_HasExtendedCapabilities(IndicatorStatus.FAIL);
            return doc;
        }

        String metadataUrl = xml.getMetadataUrlRaw();
        if ((metadataUrl == null) || (metadataUrl.isEmpty())) {
            doc.setIndicator_HasServiceMetadataLink(IndicatorStatus.FAIL);
            return doc;
        }

        doc.setIndicator_HasServiceMetadataLink(IndicatorStatus.PASS);



        List<CapabilitiesDatasetMetadataLink> dslinks = capabilitiesDatasetMetadataLinkService.createCapabilitiesDatasetMetadataLinks(doc, xml);
        doc.setCapabilitiesDatasetMetadataLinkList(dslinks);

        return doc;
    }
    public void run12(String... args) throws Exception {
        List<LocalDatasetMetadataRecord> records =   localDatasetMetadataRecordRepo.findByLinkCheckJobId("ef19f12a-14a9-4a71-8c79-6d1d178a3768");
        int total =records.size();
        int cap_resolves =0;
        int layer_matches = 0;

        for(LocalDatasetMetadataRecord r:records) {
            if (r.getINDICATOR_RESOLVES_TO_CAPABILITIES() > 0)
                cap_resolves++;
            if (r.getINDICATOR_LAYER_MATCHES() == IndicatorStatus.PASS)
                layer_matches++;
            else {
                Optional<DatasetDocumentLink> doc = r.getDocumentLinks().stream()
                        .filter(x->x.getCapabilitiesDocument() != null).findFirst();
                if (doc.isPresent()){
                    CapabilitiesDocument capdoc = doc.get().getCapabilitiesDocument();
                     String xmlCap = linkCheckBlobStorageRepo.findById(capdoc.getSha2()).get().getTextValue();
                     XmlCapabilitiesDocument xmlCapDoc = (XmlCapabilitiesDocument) xmlDocumentFactory.create(xmlCap);
                    List<CapabilitiesDatasetMetadataLink> dslinks = capabilitiesDatasetMetadataLinkService.createCapabilitiesDatasetMetadataLinks(capdoc, xmlCapDoc);

                    CapabilitiesDocument cd = createCap(xmlCapDoc);
                    cd= capabilitiesDocumentRepo.save(cd);
                    CapabilitiesDocument cd2=capabilitiesDocumentRepo.findById(cd.getCapabilitiesDocumentId()).get();

                    if (!capdoc.getCapabilitiesDatasetMetadataLinkList().isEmpty()) {
                         CapabilitiesDatasetMetadataLink dd = capdoc.getCapabilitiesDatasetMetadataLinkList().get(0);
                         int uu=0;
                     }
                    int ttt=0;
                }
            }
        }

        double percent_cap_resolves = ((double) cap_resolves)/total * 100.0;
        double percent_layer_matches = ((double) layer_matches)/total * 100.0;

        int tt=0;
    }
    public void run11(String... args) throws Exception {
       List<LocalServiceMetadataRecord> records =   localServiceMetadataRecordRepo.findByLinkCheckJobId("ef19f12a-14a9-4a71-8c79-6d1d178a3768");
       int total =records.size();
       int cap_resolves = 0;
       int cap_resolves_to_service = 0;
        int cap_link_to_service_fileId = 0;
        int cap_link_to_service_full_match = 0;
        int cap_ds_links_resolve = 0;
        int all_opson_match = 0;

        for(LocalServiceMetadataRecord r:records){

//            String xml = blobStorageService.findXML(r.getSha2());
//            XmlDoc xmlDoc = xmlDocumentFactory.create(xml);
//
//
//            String xmlCap = linkCheckBlobStorageRepo.findById(r.getServiceDocumentLinks().get(0).getCapabilitiesDocument().getSha2()).get().getTextValue();
//            XmlDoc xmlCapDoc = xmlDocumentFactory.create(xmlCap);

//
//            capabilitiesServiceMatchesLocalServiceIndicators.process(r);
//           capabilitiesDatasetLinksResolveIndicators.process(r);
//            serviceOperatesOnIndicators.process(r);

            if (r.getINDICATOR_ALL_OPERATES_ON_MATCH_CAPABILITIES() == IndicatorStatus.PASS)
                all_opson_match++;
            else {

//            String xml = blobStorageService.findXML(r.getSha2());
//            XmlDoc xmlDoc = xmlDocumentFactory.create(xml);
//
              if (r.getServiceDocumentLinks().get(0).getCapabilitiesDocument() != null) {
                  String xmlCap = linkCheckBlobStorageRepo.findById(r.getServiceDocumentLinks().get(0).getCapabilitiesDocument().getSha2()).get().getTextValue();
                  XmlDoc xmlCapDoc = xmlDocumentFactory.create(xmlCap);

                  int tta=1;
              }

                int tttt=0;
            }

           if (r.getINDICATOR_RESOLVES_TO_CAPABILITIES() >0)
                cap_resolves++;

           if (r.getINDICATOR_CAPABILITIES_RESOLVES_TO_SERVICE() == IndicatorStatus.PASS)
               cap_resolves_to_service++;
           else {
               int ttttt = 0;
           }

           if (r.getINDICATOR_CAPABILITIES_SERVICE_FILE_ID_MATCHES() == IndicatorStatus.PASS)
               cap_link_to_service_fileId++;
           if (r.getINDICATOR_CAPABILITIES_SERVICE_FULLY_MATCHES() == IndicatorStatus.PASS)
               cap_link_to_service_full_match++;

           if (r.getINDICATOR_ALL_CAPABILITIES_LAYER_RESOLVE() == IndicatorStatus.PASS)
               cap_ds_links_resolve++;

           int ttt=0;
       }

       double percent_cap_resolves = ((double) cap_resolves)/total * 100.0;
       double percent_cap_resolves_to_service = ((double) cap_resolves_to_service)/total * 100.0;

       double percent_cap_to_service_fileID = ((double) cap_link_to_service_fileId)/total * 100.0;
       double percent_cap_to_service_full = ((double) cap_link_to_service_full_match)/total * 100.0;

       double percent_cap_ds_links_resolve = ((double) cap_ds_links_resolve)/total * 100.0;
        double percent_all_ops_on_matches = ((double) all_opson_match)/total * 100.0;

        int t=0;
    }

        public void run10(String... args) throws Exception {
        LocalServiceMetadataRecord r =localServiceMetadataRecordRepo.findById(1L).get();
        String xml = blobStorageService.findXML(r.getSha2());
        XmlDoc xmlDoc = xmlDocumentFactory.create(xml);


         String xmlCap = linkCheckBlobStorageRepo.findById(r.getServiceDocumentLinks().get(0).getCapabilitiesDocument().getSha2()).get().getTextValue();
        XmlDoc xmlCapDoc = xmlDocumentFactory.create(xmlCap);

        capabilitiesServiceLinkIndicators.process(r);

    }

        public void run9(String... args) throws Exception {
        for(LocalServiceMetadataRecord localServiceMetadataRecord : localServiceMetadataRecordRepo.findAll()){
            capabilitiesServiceLinkIndicators.process(localServiceMetadataRecord);
        }
//        for(LocalDatasetMetadataRecord localDatasetMetadataRecord : localDatasetMetadataRecordRepo.findAll()){
//            capabilitiesResolvesIndicators.process(localDatasetMetadataRecord);
//        }

    }

        public void run8(String... args) throws Exception {
        Iterable<LocalDatasetMetadataRecord> records =  localDatasetMetadataRecordRepo.findAll();

        for (LocalDatasetMetadataRecord r:records){
            //String xml = linkCheckBlobStorageRepo.findById(r.getSha2()).get().getTextValue();

            String xml = blobStorageService.findXML(r.getSha2());
            XmlDoc xmlDoc = xmlDocumentFactory.create(xml);

            int t=0;
        }
    }

    public void run7(String... args) throws Exception {
      //  LinkCheckBlobStorage r1 =  linkCheckBlobStorageRepo.findById("16ADA3D6D77421D38E957705B7E7C06ABF2270242C04D669426C3814F5F6D584").get();
      //  LinkCheckBlobStorage r2 = linkCheckBlobStorageRepo.findById("988232D8B3AEDF0782B0D290C0DCBB324ACDAEFD98F606C44304B0444D7B658E").get();
        List<CapabilitiesDocument> docs = capabilitiesDocumentRepo.findByCapabilitiesDocumentType(CapabilitiesType.WFS);

        List<LinkCheckBlobStorage> xmls = docs.stream().map(x->linkCheckBlobStorageRepo.findById(x.getSha2()).get()).collect(Collectors.toList());

//        List<XmlDoc> docs2 = new ArrayList<>();
//        for (LinkCheckBlobStorage x : xmls) {
//            XmlDoc xmlDoc = xmlDocumentFactory.create(x.getTextValue());
//            docs2.add(xmlDoc);
//        }

        for(CapabilitiesDocument doc : docs) {
            ServiceMetadataRecord localService = doc.getServiceDocumentLink().getLocalServiceMetadataRecord();
            List<OperatesOnLink> opsOns = localService.getOperatesOnLinks();
            List<CapabilitiesDatasetMetadataLink> capDSs = doc.getCapabilitiesDatasetMetadataLinkList();
            if ( (opsOns.size() ==1) && (capDSs.size() == 1) ) {
                OperatesOnLink opOn = opsOns.get(0);
                OperatesOnRemoteDatasetMetadataRecord OpOnDSMD = opOn.getDatasetMetadataRecord();
                CapabilitiesDatasetMetadataLink capDSLink = capDSs.get(0);
                CapabilitiesRemoteDatasetMetadataDocument capDSMD = capDSLink.getCapabilitiesRemoteDatasetMetadataDocument();
                if ( (OpOnDSMD != null) && (capDSMD !=null)){
                    int tt=0;
                }
            }
            int t=0;
        }


        int t=0;
    }

    public void run6(String... args) throws Exception {
        List<StatusQueryItem> items = localServiceMetadataRecordRepo.getStatus("6da77404-7427-4c88-80a3-0e8eb83966ea");
        int t=0;
    }
        public void run5(String... args) throws Exception {
        List<LocalServiceMetadataRecord> localServiceMetadataRecords = localServiceMetadataRecordRepo.findByLinkCheckJobId("6fa41298-a2c7-44c6-b1b1-55d04064144d");
        for(LocalServiceMetadataRecord record : localServiceMetadataRecords){
            String human = record.getHumanReadable();
            String id = record.getFileIdentifier();
            try (PrintStream out = new PrintStream(new FileOutputStream(id+".txt"))) {
                out.print(human);
            }
        }
    }

        public void run4(String... args) throws Exception {
        LocalServiceMetadataRecord localServiceMetadataRecord = localServiceMetadataRecordRepo.findById(1132L).get();
        LocalServiceMetadataRecord smr = localServiceMetadataRecordRepo.save(localServiceMetadataRecord);

        List<LocalServiceMetadataRecord> list = Arrays.asList(new LocalServiceMetadataRecord[]{smr});

    }


        public void run3(String... args) throws Exception {
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
                metadataDocumentFactory.createLocalServiceMetadataRecord(doc, metadataRecord.getMetadataRecordId(), "TESTCASE", sha2);

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
        RemoteServiceMetadataRecordLink cd_rsmrl = cd.getRemoteServiceMetadataRecordLink();
        String ss = cd_rsmrl.toString();
        List<OperatesOnLink> ll = sm11.getOperatesOnLinks();
        int t6t = ll.size();
        String human = humanReadableServiceMetadata.getHumanReadable(sm11);

        LocalServiceMetadataRecord smr = localServiceMetadataRecordRepo.save(localServiceMetadataRecord);

        List<LocalServiceMetadataRecord> list = Arrays.asList(new LocalServiceMetadataRecord[]{smr});
            process(list);

            int t = 0;
    }

    private void process(List<LocalServiceMetadataRecord> list) throws Exception {
        for (LocalServiceMetadataRecord record : list) {

            List<RemoteServiceMetadataRecordLink> remoteLinks = new ArrayList<>();
            List<CapabilitiesDatasetMetadataLink> capabilitiesDatasetMetadataLinkList = new ArrayList<>();

            //process servicedocumentlinks
            for (ServiceDocumentLink link : record.getServiceDocumentLinks()) {
                link = serviceDocumentLinkRepo.findById(link.getServiceMetadataLinkId()).get();// make sure we re-load
                ServiceDocumentLink link2 = (ServiceDocumentLink) retrieveServiceDocumentLink.process(link);
                link2 = serviceDocumentLinkRepo.save(link2); //re-save
                if (link2.getCapabilitiesDocument() != null) {
                    CapabilitiesDocument capabilitiesDocument = link2.getCapabilitiesDocument();
                    RemoteServiceMetadataRecordLink rsmrl = capabilitiesDocument.getRemoteServiceMetadataRecordLink();
                    if (rsmrl != null) {
                        remoteLinks.add(rsmrl);
                    }
                    for (CapabilitiesDatasetMetadataLink capabilitiesDatasetMetadataLink : capabilitiesDocument.getCapabilitiesDatasetMetadataLinkList()) {
                        capabilitiesDatasetMetadataLinkList.add(capabilitiesDatasetMetadataLink);
                    }
                }

            }

            //process resulting remoteMetadataRecordLinks
            for (RemoteServiceMetadataRecordLink link : remoteLinks) {
                RemoteServiceMetadataRecordLink link2 = remoteServiceMetadataRecordLinkRepo.findById(link.getRemoteServiceMetadataRecordLinkId()).get();
                //   CapabilitiesDocument cd = link2.getCapabilitiesDocument();
                RemoteServiceMetadataRecordLink rsmrl = remoteServiceMetadataRecordLinkRetriever.process(link2);
                RemoteServiceMetadataRecordLink rsmrl2 = remoteServiceMetadataRecordLinkRepo.save(rsmrl);
                int t = 0;
            }

            //process resulting capabilities doc dataset links
            for (CapabilitiesDatasetMetadataLink link : capabilitiesDatasetMetadataLinkList) {
                CapabilitiesDatasetMetadataLink link2 = capabilitiesDatasetMetadataLinkRepo.findById(link.getCapabilitiesDatasetMetadataLinkId()).get();
                //   CapabilitiesDocument cd = link2.getCapabilitiesDocument();
                CapabilitiesDatasetMetadataLink cdml = retrieveCapabilitiesDatasetMetadataLink.process(link2);
                CapabilitiesDatasetMetadataLink cdml2 = capabilitiesDatasetMetadataLinkRepo.save(cdml);
                int t = 0;
            }


            for (OperatesOnLink operatesOnLink : record.getOperatesOnLinks()) {
                operatesOnLink = operatesOnLinkRepo.findById(operatesOnLink.getOperatesOnLinkId()).get(); //reload
                OperatesOnLink operatesOnLink2 = retrieveOperatesOnLink.process(operatesOnLink);
                operatesOnLink2 = operatesOnLinkRepo.save(operatesOnLink2);
                int tt = 0;
            }
        }
    }

    public void run2(String... args) throws Exception {

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

