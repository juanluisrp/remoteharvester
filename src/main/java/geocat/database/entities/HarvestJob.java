package geocat.database.entities;

import javax.persistence.*;

// create table harvest_job (long_term_tag text, job_id varchar(40), state varchar(40), look_for_nested_discovery_service bool, filter text, initial_url text, messages text);
@Entity

public class HarvestJob {
    @Id
    @Column(columnDefinition = "varchar(40)")
    private String jobId;
    public String initialUrl;
    @Column(columnDefinition = "text")
    private String messages;
    private String longTermTag;
    private boolean lookForNestedDiscoveryService;
    @Column(columnDefinition = "text")
    private String filter;
    @Enumerated(EnumType.STRING)
    private HarvestJobState state;
    @Column(columnDefinition = "text")
    private String problematicResultsConfigurationJSON;
    int nrecordsPerRequest;
    String getRecordQueueHint;


    public String getGetRecordQueueHint() {
        return getRecordQueueHint;
    }

    public void setGetRecordQueueHint(String getRecordQueueHint) {
        this.getRecordQueueHint = getRecordQueueHint;
    }




    public String getProblematicResultsConfigurationJSON() {
        return problematicResultsConfigurationJSON;
    }

    public void setProblematicResultsConfigurationJSON(String problematicResultsConfigurationJSON) {
        this.problematicResultsConfigurationJSON = problematicResultsConfigurationJSON;
    }



    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getInitialUrl() {
        return initialUrl;
    }

    public void setInitialUrl(String initalUrl) {
        this.initialUrl = initalUrl;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getLongTermTag() {
        return longTermTag;
    }

    public void setLongTermTag(String longTermTag) {
        this.longTermTag = longTermTag;
    }

    public boolean isLookForNestedDiscoveryService() {
        return lookForNestedDiscoveryService;
    }

    public void setLookForNestedDiscoveryService(boolean lookForNestedDiscoveryService) {
        this.lookForNestedDiscoveryService = lookForNestedDiscoveryService;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public HarvestJobState getState() {
        return state;
    }

    public void setState(HarvestJobState state) {
        this.state = state;
    }

    public int getNrecordsPerRequest() {
        return nrecordsPerRequest;
    }

    public void setNrecordsPerRequest(int nrecordsPerRequest) {
        this.nrecordsPerRequest = nrecordsPerRequest;
    }
}
