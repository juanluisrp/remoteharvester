package com.geocat.ingester.model.harvester;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.time.ZoneId;
import java.time.ZonedDateTime;


//create table endpoint_job ( endpoint_job_id varchar(40), harvest_job_id varchar(40), state varchar(40), url varchar(255), url_get_records varchar(255), expected_number_of_records int, filter text,look_for_nested_discovery_service bool);
@Entity
@Table(name="endpoint_job",
    indexes= {
        @Index(
                name="harvestJobId_idx",
                columnList="harvestJobId",
                unique=false
        )
    })

public class EndpointJob {
    @Column(columnDefinition = "timestamp with time zone", name = "create_timeutc")
    ZonedDateTime createTimeUTC;
    @Column(columnDefinition = "timestamp with time zone", name = "last_updateutc")
    ZonedDateTime lastUpdateUTC;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long endpointJobId;
    @Column(columnDefinition = "varchar(40)")
    private String harvestJobId;
    private String url;
    private boolean lookForNestedDiscoveryService;
    @Column(columnDefinition = "text")
    private String filter;
    private Integer expectedNumberOfRecords;
    private String urlGetRecords;
    @Enumerated(EnumType.STRING)
    private EndpointJobState state;

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

    public Integer getExpectedNumberOfRecords() {
        return expectedNumberOfRecords;
    }

    public void setExpectedNumberOfRecords(int expectedNumberOfRecords) {
        this.expectedNumberOfRecords = expectedNumberOfRecords;
    }

    public String getUrlGetRecords() {
        return urlGetRecords;
    }

    public void setUrlGetRecords(String urlGetRecords) {
        this.urlGetRecords = urlGetRecords;
    }

    public EndpointJobState getState() {
        return state;
    }

    public void setState(EndpointJobState state) {
        this.state = state;
    }

    public long getEndpointJobId() {
        return endpointJobId;
    }

    public void setEndpointJobId(long endpointJobId) {
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
