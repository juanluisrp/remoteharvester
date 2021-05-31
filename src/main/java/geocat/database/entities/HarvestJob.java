package geocat.database.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

// create table harvest_job (long_term_tag text, job_id varchar(40), state varchar(40), look_for_nested_discovery_service bool, filter text, initial_url text, messages text);
@Entity
public class HarvestJob {

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    private String messages;



    public String getInitialUrl() {
        return initialUrl;
    }

    public void setInitialUrl(String initalUrl) {
        this.initialUrl = initalUrl;
    }

    public String initialUrl;

    public String getFilter() {
        return filter;
    }


    public String getLongTermTag() {
        return longTermTag;
    }

    public void setLongTermTag(String longTermTag) {
        this.longTermTag = longTermTag;
    }

    private String longTermTag;


    public boolean isLookForNestedDiscoveryService() {
        return lookForNestedDiscoveryService;
    }

    public void setLookForNestedDiscoveryService(boolean lookForNestedDiscoveryService) {
        this.lookForNestedDiscoveryService = lookForNestedDiscoveryService;
    }

    private boolean lookForNestedDiscoveryService;

    public void setFilter(String filter) {
        this.filter = filter;
    }

    private String filter;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    @Id
    private String jobId;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    private String state;

 }
