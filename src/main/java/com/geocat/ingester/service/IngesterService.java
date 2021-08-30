package com.geocat.ingester.service;

import com.geocat.ingester.dao.harvester.EndpointJobRepo;
import com.geocat.ingester.dao.harvester.HarvestJobRepo;
import com.geocat.ingester.dao.harvester.MetadataRecordRepo;
import com.geocat.ingester.dao.linkchecker.LinkCheckJobRepo;
import com.geocat.ingester.dao.linkchecker.LocalDatasetMetadataRecordRepo;
import com.geocat.ingester.dao.linkchecker.LocalServiceMetadataRecordRepo;
import com.geocat.ingester.exception.GeoNetworkClientException;
import com.geocat.ingester.geonetwork.client.GeoNetworkClient;
import com.geocat.ingester.model.harvester.EndpointJob;
import com.geocat.ingester.model.harvester.HarvestJob;
import com.geocat.ingester.model.harvester.MetadataRecordXml;
import com.geocat.ingester.model.ingester.IngestJobState;
import com.geocat.ingester.model.linkchecker.LinkCheckJob;
import com.geocat.ingester.model.linkchecker.LocalDatasetMetadataRecord;
import com.geocat.ingester.model.linkchecker.LocalServiceMetadataRecord;
import com.geocat.ingester.model.metadata.HarvesterConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    @Autowired
    private LinkCheckJobRepo linkCheckJobRepo;

    @Autowired
    private LocalServiceMetadataRecordRepo localServiceMetadataRecordRepo;

    @Autowired
    private LocalDatasetMetadataRecordRepo localDatasetMetadataRecordRepo;

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

        String harvestJobId = harvestJob.get().getJobId();

        Optional<LinkCheckJob> linkCheckJob = linkCheckJobRepo.findByHarvestJobId(harvestJobId);
        if (!linkCheckJob.isPresent()) {
            log.info("No link checker job related found for the harvester with name/uuid " +  harvesterUuidOrName + ".");
            // TODO: throw Exception harvester job not found
            return;
        }

        String linkCheckJobId = linkCheckJob.get().getJobId();

        List<EndpointJob> endpointJobList = endpointJobRepo.findByHarvestJobId(harvestJobId);

        long totalMetadataToProcess = 0;
        for (EndpointJob job : endpointJobList) {
            totalMetadataToProcess = totalMetadataToProcess + metadataRecordRepo.countMetadataRecordByEndpointJobId(job.getEndpointJobId());
        }

        ingestJobService.updateIngestJobStateInDBIngestedRecords(processId, 0, 0, 0, totalMetadataToProcess);

        //List<Integer> metadataIds = new ArrayList<>();
        Map<String, Boolean> metadataIds = new HashMap<>();

        try {
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

                    metadataRecordList.forEach(r -> {
                        List<LocalServiceMetadataRecord> localServiceMetadataRecord = localServiceMetadataRecordRepo.findAllByFileIdentifierAndLinkCheckJobId(r.getRecordIdentifier(), linkCheckJobId);

                        if (!localServiceMetadataRecord.isEmpty()) {
                            if (localServiceMetadataRecord.get(0).getINDICATOR_ALL_CAPABILITIES_LAYER_RESOLVE() != null) {
                                r.addIndicator("INDICATOR_ALL_CAPABILITIES_LAYER_RESOLVE", localServiceMetadataRecord.get(0).getINDICATOR_ALL_CAPABILITIES_LAYER_RESOLVE().toString());
                            }

                            if (localServiceMetadataRecord.get(0).getINDICATOR_ALL_OPERATES_ON_RESOLVE() != null) {
                                r.addIndicator("INDICATOR_ALL_OPERATES_ON_RESOLVE", localServiceMetadataRecord.get(0).getINDICATOR_ALL_OPERATES_ON_RESOLVE().toString());
                            }

                            if (localServiceMetadataRecord.get(0).getINDICATOR_ALL_OPERATES_ON_MATCH_CAPABILITIES() != null) {
                                r.addIndicator("INDICATOR_ALL_OPERATES_ON_MATCH_CAPABILITIES", localServiceMetadataRecord.get(0).getINDICATOR_ALL_OPERATES_ON_MATCH_CAPABILITIES().toString());
                            }

                            if (localServiceMetadataRecord.get(0).getINDICATOR_CAPABILITIES_TYPE() != null) {
                                r.addIndicator("INDICATOR_CAPABILITIES_TYPE", localServiceMetadataRecord.get(0).getINDICATOR_CAPABILITIES_TYPE().toString());
                            }

                            if (localServiceMetadataRecord.get(0).getINDICATOR_CAPABILITIES_RESOLVES_TO_SERVICE() != null) {
                                r.addIndicator("INDICATOR_CAPABILITIES_RESOLVES_TO_SERVICE", localServiceMetadataRecord.get(0).getINDICATOR_CAPABILITIES_RESOLVES_TO_SERVICE().toString());
                            }

                            if (localServiceMetadataRecord.get(0).getINDICATOR_CAPABILITIES_SERVICE_FILE_ID_MATCHES() != null) {
                                r.addIndicator("INDICATOR_CAPABILITIES_SERVICE_FILE_ID_MATCHES", localServiceMetadataRecord.get(0).getINDICATOR_CAPABILITIES_SERVICE_FILE_ID_MATCHES().toString());
                            }

                            if (localServiceMetadataRecord.get(0).getINDICATOR_CAPABILITIES_SERVICE_FULLY_MATCHES() != null) {
                                r.addIndicator("INDICATOR_CAPABILITIES_SERVICE_FULLY_MATCHES", localServiceMetadataRecord.get(0).getINDICATOR_CAPABILITIES_SERVICE_FULLY_MATCHES().toString());
                            }

                            if (localServiceMetadataRecord.get(0).getINDICATOR_RESOLVES_TO_CAPABILITIES() != null) {
                                r.addIndicator("INDICATOR_RESOLVES_TO_CAPABILITIES", localServiceMetadataRecord.get(0).getINDICATOR_RESOLVES_TO_CAPABILITIES().toString());
                            }
                        } else {
                            List<LocalDatasetMetadataRecord> localDatasetMetadataRecord = localDatasetMetadataRecordRepo.findAllByFileIdentifierAndLinkCheckJobId(r.getRecordIdentifier(), linkCheckJobId);

                            if (!localDatasetMetadataRecord.isEmpty()) {
                                if (localDatasetMetadataRecord.get(0).getINDICATOR_CAPABILITIES_TYPE() != null) {
                                    r.addIndicator("INDICATOR_CAPABILITIES_TYPE", localDatasetMetadataRecord.get(0).getINDICATOR_CAPABILITIES_TYPE().toString());
                                }

                                if (localDatasetMetadataRecord.get(0).getINDICATOR_LAYER_MATCHES() != null) {
                                    r.addIndicator("INDICATOR_LAYER_MATCHES", localDatasetMetadataRecord.get(0).getINDICATOR_LAYER_MATCHES().toString());
                                }

                                if (localDatasetMetadataRecord.get(0).getINDICATOR_RESOLVES_TO_CAPABILITIES() != null) {
                                    r.addIndicator("INDICATOR_RESOLVES_TO_CAPABILITIES", localDatasetMetadataRecord.get(0).getINDICATOR_RESOLVES_TO_CAPABILITIES().toString());
                                }
                            }

                        }

                    });
                    metadataIds.putAll(catalogueService.addOrUpdateMetadataRecords(metadataRecordList.toList(), harvesterConfigurationOptional.get(), harvestJobId));

                    ingestJobService.updateIngestJobStateInDBIngestedRecords(processId, total);

                    pagesAvailable = !metadataRecordList.isLast();
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
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
