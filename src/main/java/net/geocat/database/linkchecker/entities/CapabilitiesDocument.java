package net.geocat.database.linkchecker.entities;

import net.geocat.database.linkchecker.entities2.IndicatorStatus;
import net.geocat.xml.helpers.CapabilitiesType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
public class CapabilitiesDocument {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long capabilitiesDocumentId;

    @Column(columnDefinition = "varchar(64)")
    private String sha2;

    @Enumerated(EnumType.STRING)
    private CapabilitiesType capabilitiesDocumentType;
//
//    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
//    @JoinColumn(name = "serviceDocumentLinkId" )
    @OneToOne(mappedBy = "capabilitiesDocument")
    private ServiceDocumentLink serviceDocumentLink;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20)")
    private IndicatorStatus Indicator_HasExtendedCapabilities;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20)")
    private IndicatorStatus  Indicator_HasServiceMetadataLink;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "capabilitiesDocumentId" )
    private RemoteServiceMetadataRecordLink remoteServiceMetadataRecordLink;


    @OneToMany(mappedBy= "capabilitiesDocument",
            cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<CapabilitiesDatasetMetadataLink> capabilitiesDatasetMetadataLinkList;


    @Column(columnDefinition = "text")
    private String summary;

    //---------------------------------------------------------------------------

    public long getCapabilitiesDocumentId() {
        return capabilitiesDocumentId;
    }

    public void setCapabilitiesDocumentId(long capabilitiesDocumentId) {
        this.capabilitiesDocumentId = capabilitiesDocumentId;
    }

    public String getSha2() {
        return sha2;
    }

    public void setSha2(String sha2) {
        this.sha2 = sha2;
    }

    public ServiceDocumentLink getServiceDocumentLink() {
        return serviceDocumentLink;
    }

    public void setServiceDocumentLink(ServiceDocumentLink serviceDocumentLink) {
        this.serviceDocumentLink = serviceDocumentLink;
    }

    public CapabilitiesType getCapabilitiesDocumentType() {
        return capabilitiesDocumentType;
    }

    public void setCapabilitiesDocumentType(CapabilitiesType capabilitiesDocumentType) {
        this.capabilitiesDocumentType = capabilitiesDocumentType;
    }

    public IndicatorStatus getIndicator_HasExtendedCapabilities() {
        return Indicator_HasExtendedCapabilities;
    }

    public void setIndicator_HasExtendedCapabilities(IndicatorStatus indicator_HasExtendedCapabilities) {
        Indicator_HasExtendedCapabilities = indicator_HasExtendedCapabilities;
    }

    public IndicatorStatus getIndicator_HasServiceMetadataLink() {
        return Indicator_HasServiceMetadataLink;
    }

    public void setIndicator_HasServiceMetadataLink(IndicatorStatus indicator_HasServiceMetadataLink) {
        Indicator_HasServiceMetadataLink = indicator_HasServiceMetadataLink;
    }

    public RemoteServiceMetadataRecordLink getRemoteServiceMetadataRecord() {
        return remoteServiceMetadataRecordLink;
    }

    public void setRemoteServiceMetadataRecord(RemoteServiceMetadataRecordLink remoteServiceMetadataRecordLink) {
        this.remoteServiceMetadataRecordLink = remoteServiceMetadataRecordLink;
    }

    public RemoteServiceMetadataRecordLink getRemoteServiceMetadataRecordLink() {
        return remoteServiceMetadataRecordLink;
    }

    public void setRemoteServiceMetadataRecordLink(RemoteServiceMetadataRecordLink remoteServiceMetadataRecordLink) {
        this.remoteServiceMetadataRecordLink = remoteServiceMetadataRecordLink;
    }

    public List<CapabilitiesDatasetMetadataLink> getCapabilitiesDatasetMetadataLinkList() {
        return capabilitiesDatasetMetadataLinkList;
    }

    public void setCapabilitiesDatasetMetadataLinkList(List<CapabilitiesDatasetMetadataLink> capabilitiesDatasetMetadataLinkList) {
        this.capabilitiesDatasetMetadataLinkList = capabilitiesDatasetMetadataLinkList;
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
        return toString(0);
    }

    public String toString(int indentSpaces) {
        String indent = "                                                     ".substring(0,indentSpaces);
        String result = indent+"CapabilitiesDocument {\n";
        result += indent+"      capabilitiesDocumentId: "+capabilitiesDocumentId+"\n";
        if ( (sha2 != null) && (!sha2.isEmpty()) )
            result += indent+"      sha2: "+sha2+"\n";
        if  (capabilitiesDocumentType != null)
            result += indent+"      capabilitiesDocumentType: "+capabilitiesDocumentType+"\n";

        if  (Indicator_HasExtendedCapabilities != null)
            result += indent+"      Indicator_HasExtendedCapabilities: "+Indicator_HasExtendedCapabilities+"\n";
        if  (Indicator_HasServiceMetadataLink != null)
            result += indent+"      Indicator_HasServiceMetadataLink: "+Indicator_HasServiceMetadataLink+"\n";


//        if ( (serviceDocumentLink != null)   )
//            result += indent+"      serviceDocumentLink Id: "+serviceDocumentLink.getServiceMetadataLinkId()+"\n";

        if (remoteServiceMetadataRecordLink != null) {
            result += indent+"      has Remote Service Metadata link: true\n";
            result += indent+"      Remote Service Metadata URL: " + remoteServiceMetadataRecordLink.getRawURL()+"\n";
        }

        result += indent+"  }";
        return result;
    }

}
