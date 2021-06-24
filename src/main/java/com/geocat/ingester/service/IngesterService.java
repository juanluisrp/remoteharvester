package com.geocat.ingester.service;

import com.geocat.ingester.dao.harvester.EndpointJobRepo;
import com.geocat.ingester.dao.harvester.HarvestJobRepo;
import com.geocat.ingester.dao.harvester.MetadataRecordRepo;
import com.geocat.ingester.geonetwork.client.GeoNetworkClient;
import com.geocat.ingester.model.harvester.EndpointJob;
import com.geocat.ingester.model.harvester.HarvestJob;
import com.geocat.ingester.model.harvester.MetadataRecordXml;
import com.geocat.ingester.model.metadata.HarvesterConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private GeoNetworkClient geoNetworkClient;


    /**
     * Executes the ingester process.
     *
     * @param harvesterUuid
     * @throws Exception
     */
    public void run(String harvesterUuid) throws Exception {
        Optional<HarvestJob> harvestJob;

        Optional<HarvesterConfiguration> harvesterConfigurationOptional = catalogueService.retrieveHarvesterConfiguration(harvesterUuid);

        if (!harvesterConfigurationOptional.isPresent()) {
            log.info("Harvester with uuid " +  harvesterUuid + " not found.");

            // TODO: throw Exception harvester not found
            return;
        }

        // Filter most recent
        harvestJob = harvestJobRepo.findMostRecentHarvestJobByLongTermTag(harvesterUuid);
        if (!harvestJob.isPresent()) {
            log.info("No harvester job related found for the harvester with uuid " +  harvesterUuid + ".");

            // TODO: throw Exception harvester job not found
            return;
        }

        String jobId = harvestJob.get().getJobId();
        List<EndpointJob> endpointJobList = endpointJobRepo.findByHarvestJobId(jobId);

        //List<Integer> metadataIds = new ArrayList<>();
        List<String> metadataIds = new ArrayList<>();

        for (EndpointJob job : endpointJobList) {
            Boolean pagesAvailable = true;

            int page = 0;
            int size = 200;

            while (pagesAvailable) {
                Pageable pageableRequest = PageRequest.of(page++, size);

                Page<MetadataRecordXml> metadataRecordList =
                        metadataRecordRepo.findMetadataRecordWithXmlByEndpointJobId(job.getEndpointJobId(), pageableRequest);

                log.info("Processing metadata page (page size " + size + "): " +  metadataRecordList.getNumber() + " of " + metadataRecordList.getTotalPages());

                metadataIds.addAll(catalogueService.addOrUpdateMetadataRecords(metadataRecordList.toList(), harvesterConfigurationOptional.get(), jobId));

                pagesAvailable = !metadataRecordList.isLast();
            }
        }

        int batchSize = 50;

        // Index records
        int totalPages = metadataIds.size() / batchSize;

        geoNetworkClient.init();

        for (int i = 0; i < totalPages; i++) {
            try {
                log.info("Indexing metadata page (page size " + batchSize + "): " +  (i+1) + " of " + totalPages);

                geoNetworkClient.index(metadataIds.subList(i * batchSize, Math.min((i+1) * batchSize, metadataIds.size()) - 1));

            } catch (Exception ex) {
                // TODO: Handle exception
                ex.printStackTrace();
            }
        }
    }
}
