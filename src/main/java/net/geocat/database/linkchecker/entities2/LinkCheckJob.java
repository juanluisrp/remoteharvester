package net.geocat.database.linkchecker.entities2;

import javax.persistence.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity

public class LinkCheckJob {

    @Id
    @Column(columnDefinition = "varchar(40)")
    private String jobId;

    @Column(columnDefinition = "varchar(40)")
    private String harvestJobId;

    @Enumerated(EnumType.STRING)
    private LinkCheckJobState state;


    @Column(columnDefinition = "text")
    private String messages;

    @Column(columnDefinition = "timestamp with time zone")
    ZonedDateTime createTimeUTC;
    @Column(columnDefinition = "timestamp with time zone")
    ZonedDateTime lastUpdateUTC;


    @PrePersist
    private void onInsert() {
        this.createTimeUTC = ZonedDateTime.now(ZoneId.of("UTC"));
        this.lastUpdateUTC = this.createTimeUTC;
    }

    @PreUpdate
    private void onUpdate() {
        this.lastUpdateUTC = ZonedDateTime.now(ZoneId.of("UTC"));
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getHarvestJobId() {
        return harvestJobId;
    }

    public void setHarvestJobId(String harvestJobId) {
        this.harvestJobId = harvestJobId;
    }

    public LinkCheckJobState getState() {
        return state;
    }

    public void setState(LinkCheckJobState state) {
        this.state = state;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public ZonedDateTime getCreateTimeUTC() {
        return createTimeUTC;
    }

    public void setCreateTimeUTC(ZonedDateTime createTimeUTC) {
        this.createTimeUTC = createTimeUTC;
    }

    public ZonedDateTime getLastUpdateUTC() {
        return lastUpdateUTC;
    }

    public void setLastUpdateUTC(ZonedDateTime lastUpdateUTC) {
        this.lastUpdateUTC = lastUpdateUTC;
    }
}
