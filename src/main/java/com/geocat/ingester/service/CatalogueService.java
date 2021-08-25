package com.geocat.ingester.service;

import com.geocat.ingester.dao.metadata.HarvestingSettingRepository;
import com.geocat.ingester.dao.metadata.MetadataRepository;
import com.geocat.ingester.dao.metadata.OperationAllowedRepository;
import com.geocat.ingester.model.harvester.MetadataRecordXml;
import com.geocat.ingester.model.metadata.HarvesterConfiguration;
import com.geocat.ingester.model.metadata.HarvesterSetting;
import com.geocat.ingester.model.metadata.Metadata;
import com.geocat.ingester.model.metadata.MetadataIndicator;
import com.geocat.ingester.model.metadata.OperationAllowed;
import com.geocat.ingester.model.metadata.OperationAllowedId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
//@Transactional("metadataTransactionManager")
public class CatalogueService {
    private static final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Autowired
    private MetadataRepository metadataRepo;

    @Autowired
    private OperationAllowedRepository operationAllowedRepo;

    @Autowired
    private HarvestingSettingRepository harvestingSettingRepo;

    /**
     * Creates/updates a metadata record.
     *
     * @param metadataRecords
     * @param harvesterConfiguration
     * @param jobId
     * @return
     * @throws Exception
     */
    /*public List<Integer> addOrUpdateMetadataRecords(List<MetadataRecordXml> metadataRecords,
                                                    HarvesterConfiguration harvesterConfiguration,
                                                    String jobId) throws Exception {*/

    public Map<String, Boolean> addOrUpdateMetadataRecords(List<MetadataRecordXml> metadataRecords,
                HarvesterConfiguration harvesterConfiguration,
                String jobId) throws Exception {
        List<Integer> metadataIdList = new ArrayList<>();
        Map<String, Boolean> metadataUuidList = new HashMap<>();
        List<Metadata> metadataList = new ArrayList<>();
        List<OperationAllowed> operationAllowedList = new ArrayList<>();

        Set<String> harvestedUuids = new HashSet<>();

        metadataRecords.forEach(r -> {
            harvestedUuids.add(r.getRecordIdentifier());
        });

        // Load in memory the records from the harvester that are in the catalogue database
        List<Metadata> metadataInDb = metadataRepo.findAllByUuidIn(harvestedUuids);

        for(MetadataRecordXml metadataRecord: metadataRecords) {
            String metadataUuid = metadataRecord.getRecordIdentifier();

            LocalDateTime datetime = LocalDateTime.now();
            Metadata metadata;

            Optional<Metadata> metadataOptional = metadataInDb.stream().filter(m -> m.getUuid().equals(metadataUuid)).findFirst();

            if (metadataOptional.isPresent()) {
                metadata = metadataOptional.get();

                String sha2 = computeSHA2(metadata.getData());
                if (sha2.equalsIgnoreCase(metadataRecord.getSha2())) {
                    // Don't process the record, it doesn't have changes
                    metadataUuidList.put(metadataUuid, Boolean.FALSE);
                    continue;
                }
                // TODO: Check about harvester uuid
            } else {
                metadata = new Metadata();
                metadata.setUuid(metadataRecord.getRecordIdentifier());
                metadata.setHarvestUuid(harvesterConfiguration.getUuid());
                metadata.setSource(harvesterConfiguration.getUuid());
                metadata.setCreateDate(datetime.format(DATE_PATTERN));

                // TODO: Set owner
                metadata.setOwner(Integer.parseInt(harvesterConfiguration.getUserOwner()));
                metadata.setGroupOwner(Integer.parseInt(harvesterConfiguration.getGroupOwner()));
            }

            String metadataXml = metadataRecord.getTextValue();
            metadata.setData(metadataXml);

            // TODO: Get from XML
            metadata.setChangeDate(datetime.format(DATE_PATTERN));

            metadata.getIndicators().clear();

            metadataRecord.getIndicators().forEach((k, v) -> {
                MetadataIndicator metadataIndicator = new MetadataIndicator();
                metadataIndicator.setName(k);
                metadataIndicator.setValue(v);
                metadata.getIndicators().add(metadataIndicator);

            });

            metadataList.add(metadata);
        }

        metadataRepo.saveAll(metadataList);

        metadataList.forEach(metadata -> {
            metadataIdList.add(metadata.getId());
            metadataUuidList.put(metadata.getUuid(), Boolean.TRUE);
        });

        List<OperationAllowed> operationAllowedInDb =
                operationAllowedRepo.findPublicOperationAllowedByMetadataIds(new HashSet<>(metadataIdList));

        metadataList.forEach(metadata -> {
            // TODO: Review operations management
            OperationAllowedId operationAllowedId = new OperationAllowedId();
            operationAllowedId.setMetadataId(metadata.getId());
            // "ALL" group
            operationAllowedId.setGroupId(1);
            // "View" operation
            operationAllowedId.setOperationId(0);

            boolean addOperation = false;

            Optional<OperationAllowed> operationAllowedOptional = operationAllowedInDb.stream().filter(oa -> (oa.getId().equals(operationAllowedId))).findFirst();

            if (!operationAllowedOptional.isPresent()) {
                addOperation = true;
            }

            if (addOperation) {
                OperationAllowed operationAllowed = new OperationAllowed();
                operationAllowed.setId(operationAllowedId);
                operationAllowedList.add(operationAllowed);
            }
        });

        operationAllowedRepo.saveAll(operationAllowedList);

        return metadataUuidList;
    }


    public Optional<HarvesterConfiguration> retrieveHarvesterConfiguration(String harvesterUuidOrName) {
        Optional<HarvesterSetting> harvesterSettingOptional = harvestingSettingRepo.findByNameAndValue("uuid", harvesterUuidOrName);

        if (!harvesterSettingOptional.isPresent()) {
            harvesterSettingOptional = harvestingSettingRepo.findByNameAndValue("name", harvesterUuidOrName);
        }

        HarvesterConfiguration harvesterConfiguration = null;

        if (harvesterSettingOptional.isPresent()) {
            harvesterConfiguration = new HarvesterConfiguration();

            HarvesterSetting harvesterSetting = harvesterSettingOptional.get();
            HarvesterSetting harvesterSettingParent = harvesterSetting.getParent();

            List<HarvesterSetting> harvesterSettingList = harvestingSettingRepo.findAllByParent(harvesterSettingParent);

            Optional<HarvesterSetting> harvestingSettingUuid = retrieveHarvesterSetting(harvesterSettingList, "uuid");
            harvesterConfiguration.setUuid(harvestingSettingUuid.get().getValue());

            Optional<HarvesterSetting> harvestingSettingName = retrieveHarvesterSetting(harvesterSettingList, "name");
            harvesterConfiguration.setName(harvestingSettingName.get().getValue());

            Optional<HarvesterSetting> harvestingSettingUrl = retrieveHarvesterSetting(harvesterSettingList, "capabUrl");
            harvesterConfiguration.setUrl(harvestingSettingUrl.get().getValue());

            Optional<HarvesterSetting> harvestingSettingUserId = retrieveHarvesterSetting(harvesterSettingList, "ownerUser");
            harvesterConfiguration.setUserOwner(harvestingSettingUserId.get().getValue());

            Optional<HarvesterSetting> harvestingSettingGroupId = retrieveHarvesterSetting(harvesterSettingList, "ownerGroup");
            harvesterConfiguration.setGroupOwner(harvestingSettingGroupId.get().getValue());

            // TODO: Process CSW filter
            harvesterConfiguration.setCswFilter("");
        }

        return Optional.ofNullable(harvesterConfiguration);
    }


    public List<String> retrieveLocalUuidsForHarvester(String harvesterUuid) {
        return metadataRepo.findAllUuidsBySource(harvesterUuid);
    }

    public void deleteMetadata(Set<Integer> metadataIds) {
        metadataRepo.deleteAll();
    }

    private Optional<HarvesterSetting> retrieveHarvesterSetting( List<HarvesterSetting> harvesterSettingList, String name) {
        return harvesterSettingList.stream().filter(s -> s.getName().equalsIgnoreCase(name)).findFirst();
    }


    private String computeSHA2(String xml) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(xml.getBytes(StandardCharsets.UTF_8));
        String hexHash = javax.xml.bind.DatatypeConverter.printHexBinary(hash);
        return hexHash;
    }
}
