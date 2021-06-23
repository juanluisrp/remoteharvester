package com.geocat.ingester.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geocat.ingester.dao.harvester.EndpointJobRepo;
import com.geocat.ingester.dao.harvester.HarvestJobRepo;
import com.geocat.ingester.dao.harvester.MetadataRecordRepo;
import com.geocat.ingester.exception.IndexingRecordException;
import com.geocat.ingester.geonetwork.client.GeoNetworkClient;
import com.geocat.ingester.index.model.Codelist;
import com.geocat.ingester.index.model.Contact;
import com.geocat.ingester.index.model.IndexRecord;
import com.geocat.ingester.index.model.Keyword;
import com.geocat.ingester.index.model.ResourceIdentifier;
import com.geocat.ingester.index.model.ThesaurusKeywords;
import com.geocat.ingester.index.model.WrappedCodelistList;
import com.geocat.ingester.index.model.WrappedStringList;
import com.geocat.ingester.model.harvester.EndpointJob;
import com.geocat.ingester.model.harvester.HarvestJob;
import com.geocat.ingester.model.harvester.MetadataRecordXml;
import com.geocat.ingester.model.metadata.HarvesterConfiguration;
import com.geocat.ingester.model.metadata.Metadata;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
//@Transactional("metadataTransactionManager")
@Slf4j(topic = "com.geocat.ingester.service")
public class IngesterService {
    @Autowired
    private HarvestJobRepo harvestJobRepo;

    @Autowired
    private EndpointJobRepo endpointJobRepo;

    @Autowired
    private MetadataRecordRepo metadataRecordRepo;


    @Autowired
    private CatalogueService catalogueService;

    @Autowired
    private IndexingService indexingService;

    @Autowired
    private GeoNetworkClient geoNetworkClient;

    public void run(String harvesterUuid) throws Exception {
        List<Contact> contactList = new ArrayList<>();
        Contact c = new Contact();
        c.setOrganisation("organisation");
        c.setEmail("jose.garcia@geocat.net");
        c.setRole("owner");
        contactList.add(c);

        IndexRecord indexRecord = new IndexRecord();
        indexRecord.setContact(contactList);
        List<ResourceIdentifier> resourceIdentifierList = new ArrayList<>();
        ResourceIdentifier ri = new ResourceIdentifier();
        ri.setCode("code");
        ri.setLink("link");
        ri.setCodeSpace("codespace");
        resourceIdentifierList.add(ri);
        indexRecord.setResourceIdentifier(resourceIdentifierList);

        Map<String, WrappedStringList> otherProperties = new HashMap<>();
        WrappedStringList op = new WrappedStringList();
        op.getWrapped().add("0");
        op.getWrapped().add("3");
        otherProperties.put("op", op);

        WrappedStringList codelistList = new WrappedStringList();
        Codelist cl = new Codelist();
        Map<String, String> clProperties = new HashMap<>();
        clProperties.put("value", "codelistvalue2");
        cl.setProperties(clProperties);
        codelistList.getWrapped().add(cl);

        indexRecord.addOtherProperties("op", op);
        indexRecord.addOtherProperties("cl_1", codelistList);

        HashMap<String, ThesaurusKeywords> allKeywords = new HashMap<>();
        ThesaurusKeywords thesaurusKeywords = new ThesaurusKeywords();
        thesaurusKeywords.setId("GEMETINSPIREthemesversion10");
        thesaurusKeywords.setTheme("theme");
        thesaurusKeywords.setTitle("GEMETINSPIREthemesversion10");

        Keyword keyword = new Keyword();
        keyword.add("default", "KEYWORD1");
        keyword.add("langita", "KEYWORD1ITA");
        thesaurusKeywords.getKeywords().add(keyword);

        keyword = new Keyword();
        keyword.add("default", "KEYWORD2");
        keyword.add("langita", "KEYWORD2ITA");
        thesaurusKeywords.getKeywords().add(keyword);

        allKeywords.put("GEMETINSPIREthemesversion10", thesaurusKeywords);
        indexRecord.setAllKeywords(allKeywords);

        /*Map<String,   WrappedCodelistList> codelistMap = new HashMap<>();

        WrappedCodelistList codelistList = new WrappedCodelistList();
        Codelist cl = new Codelist();
        Map<String, String> clProperties = new HashMap<>();
        clProperties.put("value", "codelistvalue");
        cl.setProperties(clProperties);

        codelistList.getWrapped().add(cl);

        cl = new Codelist();
        clProperties = new HashMap<>();
        clProperties.put("value", "codelistvalue2");
        cl.setProperties(clProperties);

        codelistList.getWrapped().add(cl);

        codelistMap.put("role", codelistList);
        indexRecord.setCodelists(codelistMap);*/


        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(indexRecord);

        StringWriter writer = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(IndexRecord.class);
        Marshaller m = context.createMarshaller();
        m.marshal(indexRecord, writer);

        Optional<HarvesterConfiguration> harvesterConfigurationOptional = catalogueService.retrieveHarvesterConfiguration(harvesterUuid);

        if (!harvesterConfigurationOptional.isPresent()) {
            // TODO: throw Exception harvester not found
            return;
        }

        // Filter most recent
        Optional<HarvestJob> harvestJob = harvestJobRepo.findMostRecentHarvestJobByLongTermTag(harvesterUuid);
        if (!harvestJob.isPresent()) {
            // TODO: throw Exception harvester job not found
            return;
        }

        String jobId = harvestJob.get().getJobId();
        List<EndpointJob> endpointJobList =  endpointJobRepo.findByHarvestJobId(jobId);

        //List<Integer> metadataIds = new ArrayList<>();
        List<String> metadataIds = new ArrayList<>();

        Stopwatch stopwatch = Stopwatch.createUnstarted();

        for (EndpointJob job : endpointJobList) {
            Boolean pagesAvailable = true;

            int page = 0;
            int size = 200;

            /*pagesAvailable = false;
            metadataIds.add(69679);*/

            while (pagesAvailable) {
                // TODO: Remove
                //if (page == 2) break;

                Pageable pageableRequest = PageRequest.of(page++, size);

                stopwatch.start();

                Page<MetadataRecordXml> metadataRecordList =
                        metadataRecordRepo.findMetadataRecordWithXmlByEndpointJobId(job.getEndpointJobId(), pageableRequest);

                stopwatch.stop();
                long timeElapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);
                log.info("Execution time in milliseconds - retrieve page " + metadataRecordList.getNumber() + " of " + metadataRecordList.getTotalPages()+ " : " + timeElapsed);

                stopwatch.reset();

                stopwatch.start();

                metadataIds.addAll(catalogueService.addOrUpdateMetadataRecords(metadataRecordList.toList(), harvesterConfigurationOptional.get(), jobId));

                /*for (MetadataRecordXml metadataRecord : metadataRecordList.toList()) {

                    Metadata metadata = catalogueService.addOrUpdateMetadataRecord(metadataRecord, jobId);

                    if (metadata != null) {
                        metadataIds.add(metadata.getId());
                    }
                }*/

                stopwatch.stop();
                timeElapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);
                //log.info("Execution time in milliseconds - addOrUpdateMetadataRecord: " + timeElapsed);
                log.info("Execution time in milliseconds - process page " + metadataRecordList.getNumber() + " of " + metadataRecordList.getTotalPages()+ " : " + timeElapsed);

                stopwatch.reset();

                pagesAvailable = !metadataRecordList.isLast();
            }
        }

        // TODO: Change to 200
        int batchSize = 50; //1;

        // Index records
        // TODO: process in batches

        int totalPages = metadataIds.size() / batchSize;

        geoNetworkClient.init();

        for (int i = 0; i < totalPages; i++) {
            try {
                stopwatch.start();

                geoNetworkClient.index(metadataIds.subList(i * batchSize, Math.min((i+1) * batchSize, metadataIds.size()) - 1));
                //indexingService.indexRecords(metadataIds);
                //indexingService.indexRecords(metadataIds.subList(i * batchSize, Math.min((i+1) * batchSize, metadataIds.size()) - 1));

                stopwatch.stop();
                long timeElapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);
                stopwatch.reset();
                //log.info("Execution time in milliseconds - addOrUpdateMetadataRecord: " + timeElapsed);
                log.info("Execution time in milliseconds - indexing page " + (i+1) + " of " + totalPages + " : " + timeElapsed);
            } catch (Exception ex) {
                // TODO: Handle exception
                ex.printStackTrace();
            }
        }
    }
}
