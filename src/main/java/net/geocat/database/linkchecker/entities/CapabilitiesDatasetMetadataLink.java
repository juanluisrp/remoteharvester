package net.geocat.database.linkchecker.entities;

import net.geocat.database.linkchecker.entities.helper.PartialDownloadHint;
import net.geocat.database.linkchecker.entities.helper.RetrievableSimpleLink;
import net.geocat.database.linkchecker.entities.helper.ServiceMetadataRecord;

import javax.persistence.*;

@Entity
public class CapabilitiesDatasetMetadataLink extends RetrievableSimpleLink {

    public CapabilitiesDatasetMetadataLink() {
        this.setPartialDownloadHint(PartialDownloadHint.METADATA_ONLY);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long capabilitiesDatasetMetadataLinkId;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "datasetMetadataRecordId" )
    CapabilitiesRemoteDatasetMetadataDocument capabilitiesRemoteDatasetMetadataDocument;

    @ManyToOne(fetch=FetchType.EAGER)
    CapabilitiesDocument capabilitiesDocument;

    @Column(columnDefinition = "text" )
    String identity;

    @Column(columnDefinition = "text" )
    String summary;

    //---------------------------------------------------------------------------


    public CapabilitiesRemoteDatasetMetadataDocument getCapabilitiesRemoteDatasetMetadataDocument() {
        return capabilitiesRemoteDatasetMetadataDocument;
    }

    public void setCapabilitiesRemoteDatasetMetadataDocument(CapabilitiesRemoteDatasetMetadataDocument capabilitiesRemoteDatasetMetadataDocument) {
        this.capabilitiesRemoteDatasetMetadataDocument = capabilitiesRemoteDatasetMetadataDocument;
    }

    public long getCapabilitiesDatasetMetadataLinkId() {
        return capabilitiesDatasetMetadataLinkId;
    }

    public void setCapabilitiesDatasetMetadataLinkId(long capabilitiesDatasetMetadataLinkId) {
        this.capabilitiesDatasetMetadataLinkId = capabilitiesDatasetMetadataLinkId;
    }

    public CapabilitiesDocument getCapabilitiesDocument() {
        return capabilitiesDocument;
    }

    public void setCapabilitiesDocument(CapabilitiesDocument capabilitiesDocument) {
        this.capabilitiesDocument = capabilitiesDocument;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    //---------------------------------------------------------------------------
    @PreUpdate
    private void onUpdate() {
        this.summary = this.toString();
    }

    @PrePersist
    private void onInsert() {
        this.summary = this.toString();
    }

    //---------------------------------------------------------------------------
    @Override
    public String toString() {
        String result = "CapabilitiesDatasetMetadataLink {\n";
        result += "      capabilitiesDatasetMetadataLinkId: "+capabilitiesDatasetMetadataLinkId+"\n";
        result += "      identity: "+identity+"\n";

        result += "\n";
        result += super.toString();
        result += "\n";

        result += "  }";
        return result;
    }
}
