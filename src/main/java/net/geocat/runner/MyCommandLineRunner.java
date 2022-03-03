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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import net.geocat.database.harvester.repos.MetadataRecordRepo;
import net.geocat.database.linkchecker.entities.*;
import net.geocat.database.linkchecker.entities.helper.*;
import net.geocat.database.linkchecker.repos.*;
import net.geocat.database.linkchecker.service.*;
import net.geocat.eventprocessor.processors.processlinks.postprocessing.*;
import net.geocat.events.EventFactory;
import net.geocat.http.BasicHTTPRetriever;
import net.geocat.http.IHTTPRetriever;
import net.geocat.service.*;

import net.geocat.service.capabilities.*;
import net.geocat.service.downloadhelpers.PartialDownloadPredicateFactory;
import net.geocat.xml.*;

import net.geocat.xml.XmlDoc;
import net.geocat.xml.XmlDocumentFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Component
public class MyCommandLineRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(MyCommandLineRunner.class);

    @Autowired
    CapabilitiesDatasetMetadataLinkRepo capabilitiesDatasetMetadataLinkRepo;

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

//    @Autowired
//    LinkProcessor_SimpleLinkRequest linkProcessor_simpleLinkRequest;

//    @Autowired
//    LinkProcessor_ProcessCapDoc linkProcessor_processCapDoc;

//    @Autowired
//    LinkProcessor_GetCapLinkedMetadata linkProcessor_getCapLinkedMetadata;

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
    RetrieveCapabilitiesDatasetMetadataLink retrieveCapabilitiesDatasetMetadataLink;


    @Autowired
    HumanReadableServiceMetadata humanReadableServiceMetadata;

    @Autowired
    CapabilitiesDocumentRepo capabilitiesDocumentRepo;

    @Autowired
    LocalDatasetMetadataRecordRepo localDatasetMetadataRecordRepo;

    @Autowired
    DatasetMetadataRecordRepo datasetMetadataRecordRepo;


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

    @Autowired
    DatasetToLayerIndicators datasetToLayerIndicators;

    @Autowired
    PartialDownloadPredicateFactory partialDownloadPredicateFactory;

    @Autowired
    BasicHTTPRetriever basicHTTPRetriever;

    @Autowired
    LocalNotProcessedMetadataRecordRepo localNotProcessedMetadataRecordRepo;

    @Autowired
    LinkCheckJobService linkCheckJobService;

    @Autowired
    LinkCheckJobRepo linkCheckJobRepo;


    @Autowired
    CapabilitiesDownloadingService capabilitiesDownloadingService;

    @Autowired
    CapabilitiesLinkFixer capabilitiesLinkFixer;


    @Autowired
    DatasetLinkFixer datasetLinkFixer;

    @Autowired
    LazyLocalServiceMetadataRecordRepo lazyLocalServiceMetadataRecordRepo;

    @Autowired
    LazyLocalDatsetMetadataRecordRepo lazyLocalDatsetMetadataRecordRepo;

    @Autowired
    EventFactory eventFactory;

    @Autowired
    @Qualifier("cachingHttpRetriever")
    IHTTPRetriever retriever_cachingHttpRetriever;


    @Override
    public void run(String... args) throws Exception {

        try {

         //  single("5917E153B70E949CC07D5A8B153A588E963BA6C6EF5F01D0500314FD029114B7");
       // allDataset();

//          HttpResult r = retriever_cachingHttpRetriever.retrieveXML("GET",
//                  "https://www.geoportal.lt/geonetwork/srv/eng/csw?elementSetName=full&id=7ce59d66-159c-4b81-9951-20f801f05748&outputSchema=http://www.isotc211.org/2005/gmd&request=GetRecordById&service=CSW&version=2.0.2",
//                  null,
//                  null,
//                  partialDownloadPredicateFactory.create(PartialDownloadHint.METADATA_ONLY));
//            String s = new String(r.getData());
//
//           XmlDoc d = xmlDocumentFactory.create(s);
            int t=0;

//            run12("1bfeaf7b-28b7-430f-87c2-12e3bfb2d3f8");
//            run11("1bfeaf7b-28b7-430f-87c2-12e3bfb2d3f8");

          String country = "nl";
//         String lastLinkCheckJob = lastLinkCheckJob(country);
           // allWFS();
           // all_();

 // run_scrape(country,lastLinkCheckJob(country));
//            run_3("25a379b1-33db-450a-a9f4-4c396a29a02a\n",
//                    "5d0e1408-2409-4c19-a037-10764523379b\n",
//                    lastLinkCheckJob);
            //series();

//            Event e = eventFactory.createStartPostProcessEvent("e21f60c6-98fb-40e6-bca3-832e8970ff3e");
//            ObjectMapper m = new ObjectMapper();
//            String s = m.writeValueAsString(e);
//            int t=0;



//           CapabilitiesDocument doc =  capabilitiesDocumentRepo.findById(
//                   new SHA2JobIdCompositeKey(
//                           "7FEB464512D4054136D1E93B38C0344458807378ADE8278F331D3974C9AB09F0",
//                           "01e2d820-bb84-4093-bc4f-cc8fe6a31932" ) ).get();
//            int t=0;

// run_scrape();

       // time();
         //   time2();

//            String url = "https://geoportal.sachsen.de/md/9250fce9-8044-41f4-a884-ae148bfbcf8c";
//            url = canonicalize(url);
//            HttpResult hr = basicHTTPRetriever.retrieveXML("GET","https://www.geoportal.rlp.de/mapbender/php/mod_dataISOMetadata.php?id=4b73c630-0feb-4f00-f1b7-a7bd0397a3d2&outputFormat=iso19139",null,null,null);
//            String xml = new String(hr.getData());
//            XmlDoc xmlDoc = xmlDocumentFactory.create(xml);
//int t=0;
//            String linkCheckJobId = "883ed410-10bc-4f7b-9292-e626a2372a24";
//            String fileID = "37569840-7c18-49da-bac5-f730491591e4";
//           // LocalServiceMetadataRecord lmr = localServiceMetadataRecordRepo.findById(232L).get();
//
//            int t=0;
//
//            Optional<LocalServiceMetadataRecord> o_lmr2 = lazyLocalServiceMetadataRecordRepo.searchFirstByFileIdentifierAndLinkCheckJobId(fileID,linkCheckJobId);
//            logger.debug("------------------ hi ------------------ hi");
//            LocalServiceMetadataRecord lmr2 = o_lmr2.get();
//            logger.debug("------------------222 hi 222------------------ hi");
//
//            int tt=0;

//   run_3("731b7cd8-3c57-9b8c-a3c5-ba1c733a8e10\n",
//           "77c103b5-a571-0002-a3c5-ba1c733a8e10",
//           "");

//
//            run11("4f3f6f36-9e03-415b-ad2e-3582954444f1");
//            run12("4f3f6f36-9e03-415b-ad2e-3582954444f1");


        }
        catch(Exception e){
            int t=0;
            logger.error("startup test case error",e);
        }
        logger.debug("DONE!");
    }

    public String lastLinkCheckJob(String country){
        if (country ==null)
            return lastLinkCheckJob();

        LinkCheckJob lastJob = null;
        for(LinkCheckJob job : linkCheckJobRepo.findAll()){
            if (!job.getLongTermTag().toLowerCase().startsWith(country.toLowerCase()))
                continue;
            if (lastJob == null)
                lastJob = job;
            if (lastJob.getCreateTimeUTC().compareTo(job.getCreateTimeUTC()) <1)
                lastJob = job;
        }
        return lastJob.getJobId();
    }


    public String lastLinkCheckJob(){
        LinkCheckJob lastJob = null;
        for(LinkCheckJob job : linkCheckJobRepo.findAll()){
            if (lastJob == null)
                lastJob = job;
            if (lastJob.getCreateTimeUTC().compareTo(job.getCreateTimeUTC()) <1)
                lastJob = job;
        }
        return lastJob.getJobId();
    }

    public void allLocalDataset() throws Exception {
        List<LocalDatasetMetadataRecord> datasets = new ArrayList();
        Iterable<LocalDatasetMetadataRecord> datasetsIterator = localDatasetMetadataRecordRepo.findAll();
        datasetsIterator.forEach(datasets::add);
        for (LocalDatasetMetadataRecord record: datasetsIterator ){
            String xml = blobStorageService.findXML(record.getSha2());
            XmlDatasetMetadataDocument doc = (XmlDatasetMetadataDocument) xmlDocumentFactory.create(xml);
            if (doc.getDatasetIdentifiers().size()>1) {
                int tt=0;
            }
            int t=0;
        }


        int t = 0;
    }

    public void allDataset() throws Exception {
//        for (LocalDatasetMetadataRecord record: localDatasetMetadataRecordRepo.findAll() ){
//            String xml = blobStorageService.findXML(record.getSha2());
//            XmlDoc doc = xmlDocumentFactory.create(xml);
//            int t=0;
//        }

        List<DatasetMetadataRecord> datasets = new ArrayList();
        Iterable<DatasetMetadataRecord> datasetsIterator = datasetMetadataRecordRepo.findAll();
        datasetsIterator.forEach(datasets::add);

        logger.debug("finished reading local xml");
        List<XmlDatasetMetadataDocument> docs = new ArrayList<>();
        for (DatasetMetadataRecord record: datasets ){
            String xml = blobStorageService.findXML(record.getSha2());
            XmlDatasetMetadataDocument doc = (XmlDatasetMetadataDocument) xmlDocumentFactory.create(xml);
            docs.add(doc);
            int t=0;
        }

        logger.debug("finished parsing local xml "+ docs.size());
        datasets.clear();
        List<CapabilitiesDatasetMetadataLink> datasets2 = new ArrayList();
        Iterable<CapabilitiesDatasetMetadataLink> linksIterator = capabilitiesDatasetMetadataLinkRepo.findBySha2NotNull();
        linksIterator.forEach(datasets2::add);
        logger.debug("finished reading external xml links ");

        for (CapabilitiesDatasetMetadataLink link: datasets2 ){
            if (link.getSha2() == null)
                continue;
            String xml = linkCheckBlobStorageRepo.findById(link.getSha2()).get().getTextValue();
            XmlDatasetMetadataDocument doc = (XmlDatasetMetadataDocument) xmlDocumentFactory.create(xml);
            docs.add(doc);
            int t=0;
        }

        logger.debug("finished parsing external xml links" + datasets2.size());
        datasets2.clear();

        Map<String, List<XmlDatasetMetadataDocument>> groups =  docs.stream()
                .collect(groupingBy(XmlDatasetMetadataDocument::getFileIdentifier));

        List<Map.Entry<String, List<XmlDatasetMetadataDocument>>> result = groups.entrySet().stream()
                .filter(x -> !x.getValue().isEmpty())
                .collect(Collectors.toList());
        int t = 0;
    }

    public void allWFS() throws Exception {

        List wfs = executeSQL3("SELECT text_value FROM blob_storage WHERE text_value like '%WFS_Capabilities%'");
        for (Object v: wfs) {
            String xml = (String) v;
            XmlDoc doc = xmlDocumentFactory.create(xml);
            int t=0;
        }
    }

    public void allWMS() throws Exception {

        List wfs = executeSQL3("SELECT text_value FROM blob_storage WHERE text_value like '%WMS_Capabilities%' ");
        for (Object v: wfs) {
            String xml = (String) v;
            XmlDoc doc = xmlDocumentFactory.create(xml);
            int t=0;
        }
    }

    public void allWMTS() throws Exception {

        List wfs = executeSQL3("SELECT text_value FROM blob_storage WHERE    text_value like '%:Capabilities%'OR text_value like '%<Capabilities%'");
        for (Object v: wfs) {
            String xml = (String) v;
            XmlDoc doc = xmlDocumentFactory.create(xml);
            int t=0;
        }
    }

    public void single(String sha2) throws Exception {
        String xml = linkCheckBlobStorageRepo.findById(sha2).get().getTextValue();
        XmlDoc doc = xmlDocumentFactory.create(xml);
        int t=0;
    }

    public void all_() throws Exception {

        List wfs = executeSQL3("SELECT text_value FROM blob_storage WHERE  text_value  ilike '%codeListValue=\"dataset\"%' ");
      // List wfs = executeSQL3("SELECT text_value FROM blob_storage WHERE  text_value  ilike '%9a21b8e2-1b28-43ee-91e9-241357add698%' ");

        for (Object v: wfs) {
            String xml = (String) v;
            XmlDoc doc = xmlDocumentFactory.create(xml);
            int t=0;
        }
    }


    private void series() throws Exception {
        for (LocalNotProcessedMetadataRecord record : localNotProcessedMetadataRecordRepo.findAll()) {
            if (record.getMetadataRecordType() !=  MetadataDocumentType.Series)
                continue;
            String xmlSeries =   blobStorageService.findXML(record.getSha2());
            XmlDoc doc = xmlDocumentFactory.create(xmlSeries);
            int t=0;
        }
    }


    @Autowired
    @Qualifier("entityManagerFactory")
    LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean;

    @Autowired
    //@Qualifier("transactionManager")
    PlatformTransactionManager transactionManager;

    EntityManager entityManager;


    public void executeSQL2(String sql) {
        if (entityManager == null)
            entityManager =  localContainerEntityManagerFactoryBean.createNativeEntityManager(null);
       entityManager.getTransaction().begin();
        int n = entityManager.createNativeQuery(sql).executeUpdate();
       entityManager.getTransaction().commit();
    }

    public List executeSQL3(String sql) {
        if (entityManager == null)
            entityManager =  localContainerEntityManagerFactoryBean.createNativeEntityManager(null);
        entityManager.getTransaction().begin();
        List result =  entityManager.createNativeQuery(sql).getResultList();
        entityManager.getTransaction().commit();
        return result;
    }

    /*
    delete from scrap;
    drop table scrap;
      CREATE TABLE scrap (title text, is_view boolean, is_download boolean, country_code text,
                file_id text, local_is_view boolean , local_is_download boolean);

        update scrap set file_id = (select fileidentifier from datasetmetadatarecord where datasetmetadatarecord.title = scrap.title limit 1);
        update scrap set local_is_view = (select indicator_layer_matches_view = 'PASS' from datasetmetadatarecord where datasetmetadatarecord.fileidentifier = scrap.file_id and linkcheckjobid='54a30bc1-3900-48d6-8cd3-c9d3609ee0a9');
        update scrap set local_is_download = (select indicator_layer_matches_download = 'PASS' from datasetmetadatarecord where datasetmetadatarecord.fileidentifier = scrap.file_id and linkcheckjobid='54a30bc1-3900-48d6-8cd3-c9d3609ee0a9') ;
--delete from scrap where file_id is null;




        select file_id, title, is_view, local_is_view from scrap where is_view !=  local_is_view and is_view and title is not null order by title;
        select file_id, title, is_download, local_is_download from scrap where is_download !=  local_is_download and is_download and title is not null order by title;



     */
    public void run_scrape(String countryCode, String jobid) throws  Exception {
        try{
            String sql =  "drop table if exists scrap;";
            executeSQL2(sql);
        }
        catch (Exception e){}

        try{
            String sql =  "CREATE TABLE scrap (title text, is_view boolean, is_download boolean, country_code text,\n" +
                    "                file_id text, local_is_view boolean , local_is_download boolean);";
            executeSQL2(sql);
        }
        catch (Exception e){}

        String url = "https://inspire-geoportal.ec.europa.eu/solr/select?wt=json&q=*:*^1.0&sow=false&fq=sourceMetadataResourceLocator:*&fq=resourceType:(dataset%20OR%20series)&fq=memberStateCountryCode:%22MYCOUNTRYCODE%22&fl=id,resourceTitle,resourceTitle_*,providedTranslationLanguage,automatedTranslationLanguage,memberStateCountryCode,metadataLanguage,isDw:query($isDwQ),isVw:query($isVwQ),spatialScope&isDwQ=interoperabilityAspect:(DOWNLOAD_MATCHING_DATA_IS_AVAILABLE%20AND%20DATA_DOWNLOAD_LINK_IS_AVAILABLE)&isVwQ=interoperabilityAspect:(LAYER_MATCHING_DATA_IS_AVAILABLE)&isDwVwQ=interoperabilityAspect:(DOWNLOAD_MATCHING_DATA_IS_AVAILABLE%20AND%20DATA_DOWNLOAD_LINK_IS_AVAILABLE%20AND%20LAYER_MATCHING_DATA_IS_AVAILABLE)&sort=query($isDwVwQ)%20desc,%20query($isDwQ)%20desc,%20query($isVwQ)%20desc,%20resourceTitle%20asc&start=0&rows=300000&callback=?&json.wrf=processData_dtResults&_=1634538073094";
        url = url.replace("MYCOUNTRYCODE",countryCode.toLowerCase());

            HttpResult result = basicHTTPRetriever.retrieveJSON("GET", url, null, null, null);

            String json = new String(result.getData());
            json = json.replace("processData_dtResults(","");

          ObjectMapper m = new ObjectMapper();
      JsonNode rootNode = m.readValue(json, JsonNode.class);

       JsonNode response =  rootNode.get("response");
       ArrayNode docs = (ArrayNode) response.get("docs");

      // executeSQL2("create table delme(i int)");
       for(JsonNode doc : docs) {
            String title = doc.get("resourceTitle").asText().replace("'","''");
            boolean isView = (doc.get("isVw") != null);
            boolean isDownload = (doc.get("isDw") != null);
            String country = doc.get("memberStateCountryCode").asText();
            String sql =  String.format("INSERT INTO scrap  (title,is_view,is_download, country_code) VALUES ('%s',%s,%s,'%s') "
                    , title, String.valueOf(isView), String.valueOf(isDownload),country);
            executeSQL2(sql);
           int tt=0;

       }
        int t=0;

        String sql =  "update scrap set file_id = (select fileidentifier from datasetmetadatarecord where datasetmetadatarecord.title = scrap.title limit 1);";
        executeSQL2(sql);


        sql =  " update scrap set local_is_view = (select indicator_layer_matches_view = 'PASS' from datasetmetadatarecord where datasetmetadatarecord.fileidentifier = scrap.file_id and linkcheckjobid='"+jobid+"');";
        executeSQL2(sql);

        sql =  " update scrap set local_is_download = (select indicator_layer_matches_download = 'PASS' from datasetmetadatarecord where datasetmetadatarecord.fileidentifier = scrap.file_id and linkcheckjobid='"+jobid+"') ";
        executeSQL2(sql);
    }


    public void run_3(String dsId, String serviceId, String linkCheckJobId) throws Exception {

        dsId = dsId.trim();
        serviceId = serviceId.trim();
        linkCheckJobId = linkCheckJobId.trim();
        LocalDatasetMetadataRecord dsRecord;

        if  ( (linkCheckJobId == null) || (linkCheckJobId.isEmpty()) )
            dsRecord = localDatasetMetadataRecordRepo.findFirstByFileIdentifier(dsId);
        else
            dsRecord = localDatasetMetadataRecordRepo.findFirstByFileIdentifierAndLinkCheckJobId(dsId,linkCheckJobId);

      //  String capabilities_layer_matches_download = dsRecord.getINDICATOR_LAYER_MATCHES_DOWNLOAD().toString();
      //  String capabilities_layer_matches_view = dsRecord.getINDICATOR_LAYER_MATCHES_VIEW().toString();


        String ds_xml = blobStorageService.findXML(dsRecord.getSha2());

       // List<ServiceDocSearchResult> serviceLinks =  operatesOnLinkRepo.linkToService(dsRecord.getFileIdentifier(), dsRecord.getDatasetIdentifier(),   dsRecord.getLinkCheckJobId());
      //  List<CapabilitiesLinkResult> capLinks =  capabilitiesDatasetMetadataLinkRepo.linkToCapabilities(dsRecord.getFileIdentifier(),dsRecord.getDatasetIdentifier(), dsRecord.getLinkCheckJobId());

//        List<LocalServiceMetadataRecord> serviceDocs = serviceLinks.stream()
//                .map(x-> localServiceMetadataRecordRepo.findById(x.getServiceid()).get())
//                .collect(Collectors.toList());
//
//        List<CapabilitiesDocument> capDocs = capLinks.stream()
//                .map(x-> capabilitiesDocumentRepo.findById( new SHA2JobIdCompositeKey(x.getSha2(),x.getLinkcheckjobid())).get())
//                .collect(Collectors.toList());

        LocalServiceMetadataRecord missing;
        if  ( (linkCheckJobId == null) || (linkCheckJobId.isEmpty()) )
            missing = localServiceMetadataRecordRepo.findFirstByFileIdentifier(serviceId);
        else
            missing = localServiceMetadataRecordRepo.findFirstByFileIdentifierAndLinkCheckJobId(serviceId,linkCheckJobId);

        List<String> urls = missing.getServiceDocumentLinks().stream()
                .map(x-> {
                    try {
                        return capabilitiesLinkFixer.fix(x.getRawURL(), missing.getMetadataServiceType(),x);
                    } catch (Exception e) {
                       return null;
                    }
                })
                .collect(Collectors.toList());

        String missingXML = blobStorageService.findXML(missing.getSha2());
        XmlDoc missingXMLDoc = xmlDocumentFactory.create(missingXML);

        List<CapabilitiesDocument> missing_caps = missing.getServiceDocumentLinks().stream()
                .filter(x-> x.getSha2() != null)
                .map(x-> capabilitiesDocumentRepo.findById( new SHA2JobIdCompositeKey(x.getSha2(), missing.getLinkCheckJobId())).get() )
                .collect(Collectors.toList());
        List<String> missing_caps_xml = missing_caps.stream()
                .filter(x-> x.getSha2() != null)
                .map(x-> linkCheckBlobStorageRepo.findById(x.getSha2()).get().getTextValue() )
                .collect(Collectors.toList());

//        for(CapabilitiesDocument cap : missing_caps) {
//            List<CapabilitiesDatasetMetadataLink> links = cap.getCapabilitiesDatasetMetadataLinkList().stream()
//                        .filter(x->x.getIdentity().startsWith("MSFD_Descriptor_1_and_6"))
//                    .collect(Collectors.toList());
//            int tt=0;
//        }

        List<XmlDoc> parsed_missing_xml = missing_caps_xml.stream()
                .map(x-> {
                    try {
                        return xmlDocumentFactory.create(x);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());

//          List<ServiceDocumentLink> processed = missing.getServiceDocumentLinks().stream()
//                .map(x-> {
//                    try {
//                        x.setLinkState(LinkState.Created);
//                          capabilitiesDownloadingService.handleLink(x);
//                          return x;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        return x;
//                    }
//
//                })
//                .collect(Collectors.toList());

//        String uurl = datasetLinkFixer.fix(missing_caps.get(1).getCapabilitiesDatasetMetadataLinkList().get(101).getRawURL());
         int t=0;

    }





    public void run12(String... args) throws Exception {
        long startTime;
        long endTime;

        startTime = System.currentTimeMillis();
        String jobid= "b5b30611-0197-40f6-bd1f-fb039eab447c";
        if ( (args !=null) && (args.length==1) )
            jobid = args[0];

        List<LocalDatasetMetadataRecord> records =   lazyLocalDatsetMetadataRecordRepo.searchByLinkCheckJobId(jobid);


        endTime = System.currentTimeMillis();
        System.out.println("records  total execution time: " + (endTime - startTime));

        int total =records.size();
        int cap_resolves =0;
        int layer_matches = 0;

        int layer_matches_view = 0;
        int layer_matches_download = 0;

        int service_matches_view = 0;
        int service_matches_download = 0;

        for(LocalDatasetMetadataRecord r:records) {

//            if (r.getINDICATOR_RESOLVES_TO_CAPABILITIES() > 0)
//                cap_resolves++;
//            else {
//                 int tt=0;
//            }
            if (r.getINDICATOR_LAYER_MATCHES() == IndicatorStatus.PASS)
                layer_matches++;
            if (r.getINDICATOR_LAYER_MATCHES_DOWNLOAD() == IndicatorStatus.PASS)
                layer_matches_download++;
            if (r.getINDICATOR_LAYER_MATCHES_VIEW() == IndicatorStatus.PASS)
                layer_matches_view++;
            if (r.getINDICATOR_SERVICE_MATCHES_DOWNLOAD() == IndicatorStatus.PASS)
                service_matches_download++;
            if (r.getINDICATOR_SERVICE_MATCHES_VIEW() == IndicatorStatus.PASS)
                service_matches_view++;
        }

        double percent_cap_resolves = ((double) cap_resolves)/total * 100.0;
        double percent_layer_matches = ((double) layer_matches)/total * 100.0;
        double percent_layer_matches_view = ((double) layer_matches_view)/total * 100.0;
        double percent_layer_matches_download = ((double) layer_matches_download)/total * 100.0;

       double percent_service_matches_download = ((double) service_matches_download)/total * 100.0;
       double percent_service_matches_view = ((double) service_matches_view)/total * 100.0;


            logger.debug("Dataset");
        logger.debug("-------");
        logger.debug("% cap doc resolves: "+percent_cap_resolves+"%");
        logger.debug("% layer matches the dataset: "+percent_layer_matches+"%");
        logger.debug("% layer matches the dataset (view): "+percent_layer_matches_view+"%");
        logger.debug("% layer matches the dataset (download): "+percent_layer_matches_download+"%");
        logger.debug("% service matches the dataset (view): "+percent_service_matches_view+" ");
        logger.debug("% service matches the dataset (download): "+percent_service_matches_download+" ");

        logger.debug("");
        logger.debug("number of docs: "+total+"");
        logger.debug("number cap doc resolves: "+cap_resolves+" ");
        logger.debug("number layer matches the dataset (either via view or download): "+layer_matches+" ");
        logger.debug("number layer matches the dataset (view): "+layer_matches_view+" ");
        logger.debug("number layer matches the dataset (download): "+layer_matches_download+" ");
        logger.debug("number service matches the dataset (view): "+service_matches_view+" ");
        logger.debug("number service matches the dataset (download): "+service_matches_download+" ");

        int tt=0;
    }
    public void run11(String... args) throws Exception {
    //   List<LocalServiceMetadataRecord> records =   localServiceMetadataRecordRepo.findByLinkCheckJobId("b7ee7707-698f-41ac-8dcf-ab0c43ab297a");
       List<LocalServiceMetadataRecord> records =   lazyLocalServiceMetadataRecordRepo.searchByLinkCheckJobId(args[0]);


       int total =records.size();
       int cap_resolves = 0;
       int cap_resolves_to_service = 0;
        int cap_link_to_service_fileId = 0;
        int cap_link_to_service_full_match = 0;
        int cap_ds_links_resolve = 0;
        int all_opson_match = 0;

        for(LocalServiceMetadataRecord r:records){

            if (r.getState() == ServiceMetadataDocumentState.NOT_APPLICABLE)
                continue; // not processed

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




//                List<OperatesOnRemoteDatasetMetadataRecord> opsonlinks_docs =     opsonlinks.stream()
//                            .filter(x-> x.getDatasetMetadataRecord() !=null)
//                            .map(x->x.getDatasetMetadataRecord())
//                            .collect(Collectors.toList());

//                List<String> opson_docs_xml = opsonlinks_docs.stream()
//                        .map(x-> linkCheckBlobStorageRepo.findById(x.getSha2()).get().getTextValue())
//                        .collect(Collectors.toList());
//
//
//                List<String> opson_docs_fileid = opsonlinks_docs.stream()
//                        .map(x-> x.getFileIdentifier())
//                        .collect(Collectors.toList());
//
//
//                List<String> opson_docs_dsid = opsonlinks_docs.stream()
//                        .map(x-> x.getDatasetIdentifier())
//                        .collect(Collectors.toList());

//                List<CapabilitiesDocument> capDocs = rr.getServiceDocumentLinks().stream()
//                            .filter(x->x.getCapabilitiesDocument() != null)
//                            .map(x->x.getCapabilitiesDocument())
//                            .collect(Collectors.toList());
//                List<String> capDocs_xmls = capDocs.stream()
//                        .map(x-> linkCheckBlobStorageRepo.findById(x.getSha2()).get().getTextValue())
//                        .collect(Collectors.toList());

//                List<CapabilitiesRemoteDatasetMetadataDocument> cap_dsdocs = capDocs.stream()
//                        .map(x-> x.getCapabilitiesDatasetMetadataLinkList())
//                        .flatMap(List::stream)
//                        .map(x->x.getCapabilitiesRemoteDatasetMetadataDocument())
//                        .filter(x->x  != null)
//                        .collect(Collectors.toList());
//                List<String> cap_dsdocs_xmls = cap_dsdocs.stream()
//                        .map(x-> linkCheckBlobStorageRepo.findById(x.getSha2()).get().getTextValue())
//                        .collect(Collectors.toList());
//
//                boolean no_capabilities_doc = cap_dsdocs.isEmpty();
//                boolean no_operates_on = opsonlinks.isEmpty();

                int tta=1;
//
//              if (r.getServiceDocumentLinks().stream().findFirst().get().getCapabilitiesDocument() != null) {
//                  String xmlCap = linkCheckBlobStorageRepo.findById(r.getServiceDocumentLinks().stream().findFirst().get().getCapabilitiesDocument().getSha2()).get().getTextValue();
//                  XmlDoc xmlCapDoc = xmlDocumentFactory.create(xmlCap);

 //                 int tta=1;
 //             }

                int tttt=0;
            }

           if (r.getINDICATOR_RESOLVES_TO_CAPABILITIES() >0)
                cap_resolves++;
           else {
//               LocalServiceMetadataRecord rr = localServiceMetadataRecordRepo.fullId(r.getServiceMetadataDocumentId());
//                     String rr_xml = blobStorageService.findXML(rr.getSha2());
           }

           if (r.getINDICATOR_CAPABILITIES_RESOLVES_TO_SERVICE() == IndicatorStatus.PASS)
               cap_resolves_to_service++;
           else {

//               if (r.getINDICATOR_RESOLVES_TO_CAPABILITIES() >0) {
//                   LocalServiceMetadataRecord rr = localServiceMetadataRecordRepo.fullId(r.getServiceMetadataDocumentId());
//                    String rr_xml = blobStorageService.findXML(rr.getSha2());
//
//                    List<CapabilitiesDocument> docs = rr.getServiceDocumentLinks().stream()
//                            .filter(x->x.getCapabilitiesDocument() != null)
//                            .map(x->x.getCapabilitiesDocument())
//                            .collect(Collectors.toList());
//                    List<String> xmls = docs.stream()
//                            .map(x-> linkCheckBlobStorageRepo.findById(x.getSha2()).get().getTextValue())
//                            .collect(Collectors.toList());
//
//                    List<XmlCapabilitiesDocument> xmlDocs = xmls.stream()
//                            .map(x-> {
//                                try {
//                                    return (XmlCapabilitiesDocument) xmlDocumentFactory.create(x);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                                return null;
//                            })
//                            .collect(Collectors.toList());
//                    int u=0;
//               }
//               Optional<ServiceDocumentLink> doc = r.getServiceDocumentLinks().stream()
//                       .filter(x->x.getCapabilitiesDocument() != null).findFirst();
//               if (doc.isPresent()) {
//                   CapabilitiesDocument capdoc = doc.get().getCapabilitiesDocument();
//                   String xmlCap = linkCheckBlobStorageRepo.findById(capdoc.getSha2()).get().getTextValue();
//                   XmlCapabilitiesDocument xmlCapDoc = (XmlCapabilitiesDocument) xmlDocumentFactory.create(xmlCap);
//                   List<CapabilitiesDatasetMetadataLink> dslinks = capabilitiesDatasetMetadataLinkService.createCapabilitiesDatasetMetadataLinks(capdoc, xmlCapDoc);
//
//                   CapabilitiesDocument cd = createCap(xmlCapDoc);
//                   cd = capabilitiesDocumentRepo.save(cd);
//                   CapabilitiesDocument cd2 = capabilitiesDocumentRepo.findById(cd.getCapabilitiesDocumentId()).get();
//
//                   if (!capdoc.getCapabilitiesDatasetMetadataLinkList().isEmpty()) {
//                       CapabilitiesDatasetMetadataLink dd = capdoc.getCapabilitiesDatasetMetadataLinkList().get(0);
//                       int uu = 0;
//                   }
//                   int ttt = 0;
//               }
//               int ttttt = 0;
           }

           if (r.getINDICATOR_CAPABILITIES_SERVICE_FILE_ID_MATCHES() == IndicatorStatus.PASS)
               cap_link_to_service_fileId++;
           else {
//               if (r.getINDICATOR_CAPABILITIES_RESOLVES_TO_SERVICE() == IndicatorStatus.PASS) {
//                   LocalServiceMetadataRecord rr = localServiceMetadataRecordRepo.fullId(r.getServiceMetadataDocumentId());
//                   String rr_xml = blobStorageService.findXML(rr.getSha2());
//
//                   List<CapabilitiesDocument> docs = rr.getServiceDocumentLinks().stream()
//                           .filter(x -> x.getCapabilitiesDocument() != null)
//                           .map(x -> x.getCapabilitiesDocument())
//                           .collect(Collectors.toList());
//                   List<String> xmls = docs.stream()
//                           .map(x -> linkCheckBlobStorageRepo.findById(x.getSha2()).get().getTextValue())
//                           .collect(Collectors.toList());
//
//                   List<XmlCapabilitiesDocument> xmlDocs = xmls.stream()
//                           .map(x -> {
//                               try {
//                                   return (XmlCapabilitiesDocument) xmlDocumentFactory.create(x);
//                               } catch (Exception e) {
//                                   e.printStackTrace();
//                               }
//                               return null;
//                           })
//                           .collect(Collectors.toList());
//                   List<RemoteServiceMetadataRecordLink> cap2serviceLinks = docs.stream()
//                           .map(x->x.getRemoteServiceMetadataRecordLink())
//                           .filter (x -> x !=null)
//                           .collect(Collectors.toList());
//
//                   List<String> remoteServiceXMLs = cap2serviceLinks.stream()
//                           .filter (x-> x.getRemoteServiceMetadataRecord() != null)
//                           .map (x-> x.getRemoteServiceMetadataRecord().getSha2())
//                           .map(x-> linkCheckBlobStorageRepo.findById(x).get().getTextValue())
//                           .collect(Collectors.toList());
//
//                                   List<String> ids = cap2serviceLinks.stream()
//                           .filter (x-> x.getRemoteServiceMetadataRecord() != null)
//                           .map (x-> x.getRemoteServiceMetadataRecord() )
//                           .map(x->x.getFileIdentifier())
//                           .collect(Collectors.toList());
//
//                   String originalFileID = r.getFileIdentifier();
//
//                   int u = 0;
 //              }
           }
//           if (r.getINDICATOR_CAPABILITIES_SERVICE_FULLY_MATCHES() == IndicatorStatus.PASS)
            //   cap_link_to_service_full_match++;

           if (r.getINDICATOR_ALL_CAPABILITIES_LAYER_RESOLVE() == IndicatorStatus.PASS)
               cap_ds_links_resolve++;
           else {
//               if (r.getINDICATOR_RESOLVES_TO_CAPABILITIES() >0) {
//                   LocalServiceMetadataRecord rr = localServiceMetadataRecordRepo.fullId(r.getServiceMetadataDocumentId());
//                   String rr_xml = blobStorageService.findXML(rr.getSha2());
//
//                   List<CapabilitiesDocument> docs = rr.getServiceDocumentLinks().stream()
//                           .filter(x -> x.getCapabilitiesDocument() != null)
//                           .map(x -> x.getCapabilitiesDocument())
//                           .collect(Collectors.toList());
//                   List<String> xmls = docs.stream()
//                           .map(x -> linkCheckBlobStorageRepo.findById(x.getSha2()).get().getTextValue())
//                           .collect(Collectors.toList());
//
//                   List<XmlCapabilitiesDocument> xmlDocs = xmls.stream()
//                           .map(x -> {
//                               try {
//                                   return (XmlCapabilitiesDocument) xmlDocumentFactory.create(x);
//                               } catch (Exception e) {
//                                   e.printStackTrace();
//                               }
//                               return null;
//                           })
//                           .collect(Collectors.toList());
//
//                   List<CapabilitiesDatasetMetadataLink> ds_links_fail = docs.stream()
//                           .map(x->x.getCapabilitiesDatasetMetadataLinkList())
//                           .flatMap(List::stream)
//                           .filter (x->x != null)
//                           .filter(x-> x.getCapabilitiesRemoteDatasetMetadataDocument() == null)
//                           .collect(Collectors.toList());
//
//                   int uu=0;
//               }
           }

           int ttt=0;
       }

       double percent_cap_resolves = ((double) cap_resolves)/total * 100.0;
       double percent_cap_resolves_to_service = ((double) cap_resolves_to_service)/total * 100.0;

       double percent_cap_to_service_fileID = ((double) cap_link_to_service_fileId)/total * 100.0;
       double percent_cap_to_service_full = ((double) cap_link_to_service_full_match)/total * 100.0;

       double percent_cap_ds_links_resolve = ((double) cap_ds_links_resolve)/total * 100.0;
       double percent_all_ops_on_matches = ((double) all_opson_match)/total * 100.0;


        logger.debug("Service");
        logger.debug("-------");
        logger.debug("total: "+total);

        logger.debug("% cap doc resolves: "+percent_cap_resolves+"%");
        logger.debug("% cap doc resolves to service: "+percent_cap_resolves_to_service+"%");
        logger.debug("% cap doc's service matches original service record (fileid): "+percent_cap_to_service_fileID+"%");
        logger.debug("% cap doc's service matches original service record (full xml): "+percent_cap_to_service_full+"%");
        logger.debug("% cap doc's Dataset links resolve: "+percent_cap_ds_links_resolve+"%");
        logger.debug("% all operatas on matches : "+percent_all_ops_on_matches+"%");
        int t=0;
    }







}

