package com.geocat.ingester.model.harvester;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.time.ZoneId;
import java.time.ZonedDateTime;

// create table harvest_job (long_term_tag text, job_id varchar(40), state varchar(40), look_for_nested_discovery_service bool, filter text, initial_url text, messages text);
@Entity
@Table(name="harvest_job")
public class HarvestJob {
    public String initialUrl;
    int nrecordsPerRequest;
    String getRecordQueueHint;
    @Column(columnDefinition = "timestamp with time zone", name = "create_timeutc")
    ZonedDateTime createTimeUTC;
    @Column(columnDefinition = "timestamp with time zone", name = "last_updateutc")
    ZonedDateTime lastUpdateUTC;
    @Id
    @Column(columnDefinition = "varchar(40)")
    private String jobId;
    @Column(columnDefinition = "text")
    private String messages;
    private String longTermTag;
    private boolean lookForNestedDiscoveryService;
    @Column(columnDefinition = "text")
    private String filter;
    @Enumerated(EnumType.STRING)
    private HarvestJobState state;
    @Column(columnDefinition = "text", name = "problematic_results_configurationjson")
    private String problematicResultsConfigurationJSON;

    @PrePersist
    private void onInsert() {
        this.createTimeUTC = ZonedDateTime.now(ZoneId.of("UTC"));
        this.lastUpdateUTC = this.createTimeUTC;
    }

    @PreUpdate
    private void onUpdate() {
        this.lastUpdateUTC = ZonedDateTime.now(ZoneId.of("UTC"));
    }

    public ZonedDateTime getCreateTimeUTC() {
        return createTimeUTC;
    }

    public ZonedDateTime getLastUpdateUTC() {
        return lastUpdateUTC;
    }

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
