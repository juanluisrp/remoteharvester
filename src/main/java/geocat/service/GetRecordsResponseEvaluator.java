package geocat.service;


import geocat.database.entities.EndpointJob;
import geocat.database.entities.HarvestJob;
import geocat.model.GetRecordsResponseInfo;
import geocat.model.ProblematicResultsConfiguration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GetRecordsResponseEvaluator {


    public void evaluate(HarvestJob harvestJob, EndpointJob endpointJob, GetRecordsResponseInfo info) throws Exception {
        ProblematicResultsConfiguration problematicResultsConfiguration
                = ProblematicResultsConfiguration.parse(harvestJob.getProblematicResultsConfigurationJSON());

        evaluate_returnedRecords(harvestJob, endpointJob, info);
    }

    //looks at the number of records in the actual response (i.e. should be 20)
    private void evaluate_returnedRecords(HarvestJob harvestJob, EndpointJob endpointJob, GetRecordsResponseInfo info) {

    }
}
