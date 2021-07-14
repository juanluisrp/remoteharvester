package net.geocat.database.linkchecker.entities;

import javax.persistence.*;

@Entity
public class MetadataDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long metadataDocumentId;

    @Column(columnDefinition = "varchar(40)")
    private String linkCheckJobId;

    @Column(columnDefinition = "varchar(64)")
    private String sha2;

    @Column(columnDefinition = "text")
    private String recordIdentifier;


    private long harvesterMetadataRecordId;

    @Column(columnDefinition = "text" )
    //i.e. service/dataset
    private String metadataRecordType;

    @Column(columnDefinition = "text" )
    //i.e. view/download/discovery
    private String metadataServiceType;

    private Integer numberOfLinksFound;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20)")
    MetadataDocumentState state;

    public long getMetadataDocumentId() {
        return metadataDocumentId;
    }

    public void setMetadataDocumentId(long metadataDocumentId) {
        this.metadataDocumentId = metadataDocumentId;
    }

    public String getLinkCheckJobId() {
        return linkCheckJobId;
    }

    public void setLinkCheckJobId(String linkCheckJobId) {
        this.linkCheckJobId = linkCheckJobId;
    }

    public String getSha2() {
        return sha2;
    }

    public void setSha2(String sha2) {
        this.sha2 = sha2;
    }

    public String getRecordIdentifier() {
        return recordIdentifier;
    }

    public void setRecordIdentifier(String recordIdentifier) {
        this.recordIdentifier = recordIdentifier;
    }

    public long getHarvesterMetadataRecordId() {
        return harvesterMetadataRecordId;
    }

    public void setHarvesterMetadataRecordId(long harvesterMetadataRecordId) {
        this.harvesterMetadataRecordId = harvesterMetadataRecordId;
    }

    public String getMetadataRecordType() {
        return metadataRecordType;
    }

    public void setMetadataRecordType(String metadataRecordType) {
        this.metadataRecordType = metadataRecordType;
    }

    public MetadataDocumentState getState() {
        return state;
    }

    public void setState(MetadataDocumentState state) {
        this.state = state;
    }

    public String getMetadataServiceType() {
        return metadataServiceType;
    }

    public void setMetadataServiceType(String metadataServiceType) {
        this.metadataServiceType = metadataServiceType;
    }


    public Integer getNumberOfLinksFound() {
        return numberOfLinksFound;
    }

    public void setNumberOfLinksFound(Integer numberOfLinksFound) {
        this.numberOfLinksFound = numberOfLinksFound;
    }

    @Override
    public String toString(){
        String result = "MetadataDocument {\n";
        result+= "     metadataDocumentId:"+metadataDocumentId+"\n";
        result+= "     linkCheckJobId:"+linkCheckJobId+"\n";
        result+= "     sha2:"+sha2+"\n";
        result+= "     harvesterMetadataRecordId:"+harvesterMetadataRecordId+"\n";
        result+= "     state:"+state+"\n";

        result+= "     recordIdentifier:"+recordIdentifier+"\n";
        result+= "     metadataRecordType:"+metadataRecordType+"\n";
        result+= "     metadataServiceType:"+metadataServiceType+"\n";
        result+= "     numberOfLinksFound:"+numberOfLinksFound+"\n";

        result += "}";
        return result;
    }
}
