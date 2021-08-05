package com.geocat.ingester.service;

import com.geocat.ingester.dao.harvester.EndpointJobRepo;
import com.geocat.ingester.dao.harvester.HarvestJobRepo;
import com.geocat.ingester.dao.harvester.MetadataRecordRepo;
import com.geocat.ingester.exception.GeoNetworkClientException;
import com.geocat.ingester.geonetwork.client.GeoNetworkClient;
import com.geocat.ingester.model.harvester.EndpointJob;
import com.geocat.ingester.model.harvester.HarvestJob;
import com.geocat.ingester.model.harvester.MetadataRecordXml;
import com.geocat.ingester.model.ingester.IngestJobState;
import com.geocat.ingester.model.metadata.HarvesterConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Autowired
    private IngestJobService ingestJobService;

    /**
     * Executes the ingester process.
     *
     * @param harvesterUuidOrName
     * @throws Exception
     */
    public void run(String processId, String harvesterUuidOrName) throws Exception {
        Optional<HarvestJob> harvestJob;

        Optional<HarvesterConfiguration> harvesterConfigurationOptional = catalogueService.retrieveHarvesterConfiguration(harvesterUuidOrName);

        if (!harvesterConfigurationOptional.isPresent()) {
            log.info("Harvester with name/uuid " +  harvesterUuidOrName + " not found.");
            // TODO: throw Exception harvester not found
            return;
        }

        // Filter most recent
        harvestJob = harvestJobRepo.findMostRecentHarvestJobByLongTermTag(harvesterUuidOrName);
        if (!harvestJob.isPresent()) {
            log.info("No harvester job related found for the harvester with name/uuid " +  harvesterUuidOrName + ".");
            // TODO: throw Exception harvester job not found
            return;
        }

        log.info("Start ingestion process for harvester with name/uuid " +  harvesterUuidOrName + ".");

        String jobId = harvestJob.get().getJobId();
        List<EndpointJob> endpointJobList = endpointJobRepo.findByHarvestJobId(jobId);

        long totalMetadataToProcess = 0;
        for (EndpointJob job : endpointJobList) {
            totalMetadataToProcess = totalMetadataToProcess + metadataRecordRepo.countMetadataRecordByEndpointJobId(job.getEndpointJobId());
        }

        ingestJobService.updateIngestJobStateInDBIngestedRecords(processId, 0, 0, 0, totalMetadataToProcess);

        //List<Integer> metadataIds = new ArrayList<>();
        Map<String, Boolean> metadataIds = new HashMap<>();

        for (EndpointJob job : endpointJobList) {
            Boolean pagesAvailable = true;

            int page = 0;
            int size = 200;

            long total = 0;

            while (pagesAvailable) {
                Pageable pageableRequest = PageRequest.of(page++, size);

                Page<MetadataRecordXml> metadataRecordList =
                        metadataRecordRepo.findMetadataRecordWithXmlByEndpointJobId(job.getEndpointJobId(), pageableRequest);

                if (page == 1) {
                    log.info("Total harvested records to process: " + metadataRecordList.getTotalElements());
                }

                long from = (size * (page - 1) ) + 1;
                long to =  Math.min((size * page) - 1, metadataRecordList.getTotalElements());
                log.info("Adding harvested metadata records to the catalogue from " +  from + " to " + to + " of " + metadataRecordList.getTotalElements());

                total = total + metadataRecordList.getNumberOfElements();

                metadataIds.putAll(catalogueService.addOrUpdateMetadataRecords(metadataRecordList.toList(), harvesterConfigurationOptional.get(), jobId));

                ingestJobService.updateIngestJobStateInDBIngestedRecords(processId, total);

                pagesAvailable = !metadataRecordList.isLast();
            }
        }

        // Index added/updated records
        ingestJobService.updateIngestJobStateInDB(processId, IngestJobState.INDEXING_RECORDS);
        List<String> metadataIdsToIndex = metadataIds.entrySet().stream().filter(a -> a.getValue().equals(Boolean.TRUE))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        geoNetworkClient.init();
        indexRecords(metadataIdsToIndex, processId);

        // Delete old harvested records no longer in the harvester server
        List<String> remoteHarvesterUuids = metadataIds.entrySet().stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<String> localHarvesterUuids =
                catalogueService.retrieveLocalUuidsForHarvester(harvesterConfigurationOptional.get().getUuid());

        List<String> metadataIdsToDelete = localHarvesterUuids.stream()
                .distinct()
                .filter(s -> !remoteHarvesterUuids.contains(s))
                .collect(Collectors.toList());

        ingestJobService.updateIngestJobStateInDB(processId, IngestJobState.DELETING_RECORDS);
        deleteRecords(metadataIdsToDelete, processId);

        ingestJobService.updateIngestJobStateInDB(processId, IngestJobState.RECORDS_PROCESSED);

        log.info("Finished ingestion process for harvester with name/uuid " +  harvesterUuidOrName + ".");
    }


    /**
     * Calls GeoNetwork index process for a set of metadata records.
     *
     * @param metadataIds
     * @param processId
     * @throws GeoNetworkClientException
     */
    private void indexRecords(List<String> metadataIds, String processId) {
        int batchSize = 50;

        int totalPages = (int) Math.ceil(metadataIds.size() * 1.0 / batchSize * 1.0);

        int total = 0;

        for (int i = 0; i < totalPages; i++) {
            try {
                int from = i * batchSize;
                int to = Math.min(((i+1) * batchSize), metadataIds.size());

                int toR = (i == totalPages - 1)?metadataIds.size():(to-1);
                log.info("Indexing harvested metadata records from " +  Math.max(1, i * batchSize) + " to " + toR + " of " + metadataIds.size());

                geoNetworkClient.index(metadataIds.subList(from , to));

                total = total + (to-from);

                ingestJobService.updateIngestJobStateInDBIndexedRecords(processId, total);

            } catch (GeoNetworkClientException ex) {
                // TODO: Handle exception
                log.error(ex.getMessage(), ex);
            }
        }
    }

    /**
     * Calls GeoNetwork delete process for a set of metadata records.
     *
     * @param metadataIds
     * @param processId
     * @throws GeoNetworkClientException
     */
    private void deleteRecords(List<String> metadataIds, String processId) {
        int batchSize = 50;

        int totalPages = (int) Math.ceil(metadataIds.size() * 1.0 / batchSize * 1.0);

        int total = 0;

        for (int i = 0; i < totalPages; i++) {
            try {
                int from = i * batchSize;
                int to = Math.min(((i+1) * batchSize), metadataIds.size());

                int toR = (i == totalPages - 1)?metadataIds.size():(to-1);
                log.info("Indexing harvested metadata records from " +  Math.max(1, i * batchSize) + " to " + toR + " of " + metadataIds.size());

                geoNetworkClient.delete(metadataIds.subList(from , to));

                total = total + (to-from);

                ingestJobService.updateIngestJobStateInDBDeletedRecords(processId, total);

            } catch (Exception ex) {
                // TODO: Handle exception
                log.error(ex.getMessage(), ex);

            }
        }
    }
}
