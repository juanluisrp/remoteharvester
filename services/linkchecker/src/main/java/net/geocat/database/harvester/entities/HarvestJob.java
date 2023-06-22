/*
 *  =============================================================================
 *  ===  Copyright (C) 2021 Food and Agriculture Organization of the
 *  ===  United Nations (FAO-UN), United Nations World Food Programme (WFP)
 *  ===  and United Nations Environment Programme (UNEP)
 *  ===
 *  ===  This program is free software; you can redistribute it and/or modify
 *  ===  it under the terms of the GNU General Public License as published by
 *  ===  the Free Software Foundation; either version 2 of the License, or (at
 *  ===  your option) any later version.
 *  ===
 *  ===  This program is distributed in the hope that it will be useful, but
 *  ===  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  ===  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  ===  General Public License for more details.
 *  ===
 *  ===  You should have received a copy of the GNU General Public License
 *  ===  along with this program; if not, write to the Free Software
 *  ===  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 *  ===
 *  ===  Contact: Jeroen Ticheler - FAO - Viale delle Terme di Caracalla 2,
 *  ===  Rome - Italy. email: geonetwork@osgeo.org
 *  ===
 *  ===  Development of this program was financed by the European Union within
 *  ===  Service Contract NUMBER – 941143 – IPR – 2021 with subject matter
 *  ===  "Facilitating a sustainable evolution and maintenance of the INSPIRE
 *  ===  Geoportal", performed in the period 2021-2023.
 *  ===
 *  ===  Contact: JRC Unit B.6 Digital Economy, Via Enrico Fermi 2749,
 *  ===  21027 Ispra, Italy. email: JRC-INSPIRE-SUPPORT@ec.europa.eu
 *  ==============================================================================
 */

package net.geocat.database.harvester.entities;

import javax.persistence.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;

// create table harvest_job (long_term_tag text, job_id varchar(40), state varchar(40), look_for_nested_discovery_service bool, filter text, initial_url text, messages text);
@Entity
@Table(name = "harvest_job")
public class HarvestJob {

    @Column(name = "initial_url")
    public String initialUrl;

    @Column(name = "nrecords_per_request")
    int nrecordsPerRequest;

    @Column(name = "get_record_queue_hint")
    String getRecordQueueHint;
    @Column(name = "create_time_utc", columnDefinition = "timestamp with time zone")
    ZonedDateTime createTimeUTC;
    @Column(name = "last_update_utc", columnDefinition = "timestamp with time zone")
    ZonedDateTime lastUpdateUTC;
    @Id
    @Column(name = "job_id", columnDefinition = "varchar(40)")
    private String jobId;
    @Column(name = "message", columnDefinition = "text")
    private String messages;
    @Column(name = "long_term_tag")
    private String longTermTag;
    @Column(name = "look_for_nested_discovery_service")
    private boolean lookForNestedDiscoveryService;
    @Column(columnDefinition = "text")
    private String filter;
    @Enumerated(EnumType.STRING)
    private HarvestJobState state;
    @Column(name = "problematic_results_configuration_json", columnDefinition = "text")
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
