package net.geocat.database.linkchecker.entities;

import net.geocat.database.linkchecker.entities.helper.PartialDownloadHint;
import net.geocat.database.linkchecker.entities.helper.RetrievableSimpleLink;
import net.geocat.database.linkchecker.entities.helper.ServiceMetadataRecord;

import javax.persistence.*;

@Entity
public class ServiceDocumentLink extends RetrievableSimpleLink {

    public ServiceDocumentLink() {
        this.setPartialDownloadHint(PartialDownloadHint.CAPABILITIES_ONLY);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long serviceMetadataLinkId;

    @Column(columnDefinition = "text")
    String operationName;


    @Column(columnDefinition = "text")
    String protocol;

    @Column(columnDefinition = "text")
    String function;

    @ManyToOne(fetch=FetchType.EAGER)
//    @JoinColumn(name="serviceMetadataId")
    ServiceMetadataRecord serviceMetadataRecord;


    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "capabilitiesDocumentId" )
    private CapabilitiesDocument capabilitiesDocument;

    @Column(columnDefinition = "text" )
    String summary;


    //---------------------------------------------------------------------------

     public long getServiceMetadataLinkId() {
        return serviceMetadataLinkId;
    }

    public void setServiceMetadataLinkId(long serviceMetadataLinkId) {
        this.serviceMetadataLinkId = serviceMetadataLinkId;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }


    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public  ServiceMetadataRecord getLocalServiceMetadataRecord() {
        return serviceMetadataRecord;
    }

    public void setServiceMetadataRecord(ServiceMetadataRecord localServiceMetadataRecord) {
        this.serviceMetadataRecord = localServiceMetadataRecord;
    }

    public CapabilitiesDocument getCapabilitiesDocument() {
        return capabilitiesDocument;
    }

    public void setCapabilitiesDocument(CapabilitiesDocument capabilitiesDocument) {
        this.capabilitiesDocument = capabilitiesDocument;
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
        String result = "ServiceDocumentLink {\n";
        result += "      serviceMetadataLinkId: "+serviceMetadataLinkId+"\n";
        if ( (operationName != null) && (!operationName.isEmpty()) )
            result += "      operationName: "+operationName+"\n";

        if ( (protocol != null) && (!protocol.isEmpty()) )
            result += "      protocol: "+protocol+"\n";
        if ( (function != null) && (!function.isEmpty()) )
            result += "      function: "+function+"\n";

//        if ( (serviceMetadataRecord != null)   )
//            result += "      serviceMetadataRecord record identifier: "+ serviceMetadataRecord.getFileIdentifier()+"\n";
//        if ( (serviceMetadataRecord != null)   )
//            result += "      serviceMetadataRecord Id: "+ serviceMetadataRecord.getServiceMetadataDocumentId()+"\n";

        result += "\n";
        result += super.toString();
        result += "\n";
        result += "     +  Link is Capabilities Document: "+(getCapabilitiesDocument() != null)+"\n";
        if (getCapabilitiesDocument() != null) {
            result += getCapabilitiesDocument().toString(8);
        }

        result += "\n";
        result += "  }";
        return result;
    }
}
