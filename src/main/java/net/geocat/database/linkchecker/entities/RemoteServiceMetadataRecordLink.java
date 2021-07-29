package net.geocat.database.linkchecker.entities;

import net.geocat.database.linkchecker.entities.helper.PartialDownloadHint;
import net.geocat.database.linkchecker.entities.helper.RetrievableSimpleLink;

import javax.persistence.*;

@Entity
public class RemoteServiceMetadataRecordLink extends RetrievableSimpleLink {

    public RemoteServiceMetadataRecordLink() {
        this.setPartialDownloadHint(PartialDownloadHint.METADATA_ONLY);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long remoteServiceMetadataRecordLinkId;

//    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
//    @JoinColumn(name = "capabilitiesDocumentId" )
    @OneToOne(mappedBy = "remoteServiceMetadataRecordLink")
    CapabilitiesDocument capabilitiesDocument;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "remoteServiceMetadataRecordId" )
    RemoteServiceMetadataRecord remoteServiceMetadataRecord;

    @Column(columnDefinition = "text" )
    private String summary;

    //---------------------------------------------------------------------------

    public long getRemoteServiceMetadataRecordLinkId() {
        return remoteServiceMetadataRecordLinkId;
    }

    public void setRemoteServiceMetadataRecordLinkId(long remoteServiceMetadataRecordId) {
        this.remoteServiceMetadataRecordLinkId = remoteServiceMetadataRecordId;
    }

    public CapabilitiesDocument getCapabilitiesDocument() {
        return capabilitiesDocument;
    }

    public void setCapabilitiesDocument(CapabilitiesDocument capabilitiesDocument) {
        this.capabilitiesDocument = capabilitiesDocument;
    }

    public RemoteServiceMetadataRecord getRemoteServiceMetadataRecord() {
        return remoteServiceMetadataRecord;
    }

    public void setRemoteServiceMetadataRecord(RemoteServiceMetadataRecord remoteServiceMetadataRecord) {
        this.remoteServiceMetadataRecord = remoteServiceMetadataRecord;
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
        String result = "RemoteServiceMetadataRecordLink {\n";
        result += "      remoteServiceMetadataRecordLinkId: "+remoteServiceMetadataRecordLinkId+"\n";


        result += "\n";
        result += super.toString();
        result += "\n";


        result += "      has remote Service Metadata Record :"+(remoteServiceMetadataRecord != null)+"\n";
        if (remoteServiceMetadataRecord != null){
            result += "      Remote Service Metadata Record file identifier:"+remoteServiceMetadataRecord.getFileIdentifier()+"\n";
         }
        result += "  }";
        return result;
    }
}
