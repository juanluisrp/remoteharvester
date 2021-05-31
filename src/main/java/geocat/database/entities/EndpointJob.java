package geocat.database.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

//create table endpoint_job ( endpoint_job_id varchar(40), harvest_job_id varchar(40), state varchar(40), url varchar(255), url_get_records varchar(255), expected_number_of_records int, filter text,look_for_nested_discovery_service bool);
@Entity
public class EndpointJob {

    @Id
    private String endpointJobId;
    private String harvestJobId;
    private String url;
    private boolean lookForNestedDiscoveryService;


    public boolean isLookForNestedDiscoveryService() {
        return lookForNestedDiscoveryService;
    }

    public void setLookForNestedDiscoveryService(boolean lookForNestedDiscoveryService) {
        this.lookForNestedDiscoveryService = lookForNestedDiscoveryService;
    }


    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    private String filter;

    public Integer getExpectedNumberOfRecords() {
        return expectedNumberOfRecords;
    }

    public void setExpectedNumberOfRecords(int expectedNumberOfRecords) {
        this.expectedNumberOfRecords = expectedNumberOfRecords;
    }

    private Integer expectedNumberOfRecords;

    public String getUrlGetRecords() {
        return urlGetRecords;
    }

    public void setUrlGetRecords(String urlGetRecords) {
        this.urlGetRecords = urlGetRecords;
    }

    private String urlGetRecords;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    private String state;

    public String getEndpointJobId() {
        return endpointJobId;
    }

    public void setEndpointJobId(String endpointJobId) {
        this.endpointJobId = endpointJobId;
    }

    public String getHarvestJobId() {
        return harvestJobId;
    }

    public void setHarvestJobId(String harvestJobId) {
        this.harvestJobId = harvestJobId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
