package geocat.service;


import geocat.csw.csw.XMLTools;
import geocat.database.entities.EndpointJob;
import geocat.database.entities.HarvestJob;
import geocat.database.entities.RecordSet;
import geocat.database.repos.MetadataRecordRepo;
import geocat.model.GetRecordsResponseInfo;
import geocat.model.ProblematicResultsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.NodeList;

@Component
@Scope("prototype")
public class GetRecordsResponseEvaluator {

    @Autowired
    MetadataRecordRepo metadataRecordRepo;

    public void evaluate(HarvestJob harvestJob,
                         EndpointJob endpointJob,
                         GetRecordsResponseInfo info,
                         RecordSet recordSet) throws Exception {
        ProblematicResultsConfiguration problematicResultsConfiguration
                = ProblematicResultsConfiguration.parse(harvestJob.getProblematicResultsConfigurationJSON());


        evaluate_returnedRecords(harvestJob, endpointJob, info, recordSet, problematicResultsConfiguration);
        evaluate_totalRecordsChanged(harvestJob, endpointJob, info, recordSet, problematicResultsConfiguration);
        evaluate_nextRecord(harvestJob, endpointJob, info, recordSet, problematicResultsConfiguration);
    }


    //looks at the number of records in the actual response (i.e. should be 20)
    private void evaluate_returnedRecords(HarvestJob harvestJob,
                                          EndpointJob endpointJob,
                                          GetRecordsResponseInfo info,
                                          RecordSet recordSet,
                                          ProblematicResultsConfiguration problematicResultsConfiguration) throws Exception {
        //first, verify if the reported # of records matches the actual records in the reponse
        NodeList metadataRecords = XMLTools.xpath_nodeset(info.getXmlParsed(), "/GetRecordsResponse/SearchResults/MD_Metadata");
        if (metadataRecords.getLength() != info.getNrecords())
            throw new Exception("GetRecord response - reported " + info.getNrecords() + " records, but actually are " + metadataRecords.getLength());

        // if > requested --> error
        if (info.getNrecords() > recordSet.getExpectedNumberRecords())
            throw new Exception("GetRecord response - server returned more records than requested!");

        if (info.getNrecords() != recordSet.getExpectedNumberRecords()) {
            //i.e. requested 20 records, only got 19
            if (problematicResultsConfiguration.errorIfTooFewRecordsReturnedInResponse())
                throw new Exception("GetRecord response - returned fewer records than requested - got " + info.getNrecords() + ", but expected " + recordSet.getExpectedNumberRecords());
        }
    }

    private void evaluate_totalRecordsChanged(HarvestJob harvestJob,
                                              EndpointJob endpointJob,
                                              GetRecordsResponseInfo info,
                                              RecordSet recordSet,
                                              ProblematicResultsConfiguration problematicResultsConfiguration) throws Exception {
        if (info.getTotalExpectedResults() == endpointJob.getExpectedNumberOfRecords())
            return; // all good

        //all bad
        if (problematicResultsConfiguration.errorIfTotalRecordsChanges())
            throw new Exception("GetRecord response - total records expected was " + endpointJob.getExpectedNumberOfRecords() + ", but now is " + info.getTotalExpectedResults());

        //calculate % change and see if its too high
        double change = ((double) endpointJob.getExpectedNumberOfRecords()) / ((double) info.getTotalExpectedResults()) * 100;
        double percentChange = Math.abs(change - 100.0);
        if (percentChange > problematicResultsConfiguration.getMaxPercentChangeTotalRecords())
            throw new Exception("GetRecord response - total records expected was " + endpointJob.getExpectedNumberOfRecords() + ", but now is " + info.getTotalExpectedResults() + ", this is a " + percentChange + " change, which is more than " + problematicResultsConfiguration.getMaxPercentChangeTotalRecords());
    }

    private void evaluate_nextRecord(HarvestJob harvestJob,
                                     EndpointJob endpointJob,
                                     GetRecordsResponseInfo info,
                                     RecordSet recordSet,
                                     ProblematicResultsConfiguration problematicResultsConfiguration) throws Exception {
        if (recordSet.isLastSet()) {
            if ((info.getNextRecordNumber() ==null) || (info.getNextRecordNumber() == 0))
                return; //all good

            if (problematicResultsConfiguration.errorIfLastRecordIsNotZero())
                throw new Exception("GetRecord response - this is the last record set and nextRecord=" + info.getNextRecordNumber());

            return; //done
        }

        //not the last record set
        int computedNextRecord = recordSet.getStartRecordNumber() + info.getNrecords();
        if ((info.getNextRecordNumber() != null) && (info.getNextRecordNumber() != computedNextRecord) && (problematicResultsConfiguration.errorIfNextRecordComputedWrong())) {
            throw new Exception("GetRecord response - computed NextRecord != received NextRecordNumber - " + computedNextRecord + " != " + info.getNextRecordNumber());
        }
    }

    public void evaluate_duplicateUUIDs(HarvestJob harvestJob, EndpointJob endpointJob) throws Exception {
        ProblematicResultsConfiguration problematicResultsConfiguration
                = ProblematicResultsConfiguration.parse(harvestJob.getProblematicResultsConfigurationJSON());

        long totalCount = metadataRecordRepo.countByEndpointJobId(endpointJob.getEndpointJobId());
        long distinctRecordIdentifiers = metadataRecordRepo.countDistinctRecordIdentifierByEndpointJobId(endpointJob.getEndpointJobId());

        if ((totalCount != distinctRecordIdentifiers) && (problematicResultsConfiguration.errorIfDuplicateUUIDs())) {
            throw new Exception("duplicate record uuids detected - totalCount=" + totalCount + ", distinctCount=" + distinctRecordIdentifiers);
        }
    }
}
