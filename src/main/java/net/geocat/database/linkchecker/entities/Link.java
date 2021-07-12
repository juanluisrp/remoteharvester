package net.geocat.database.linkchecker.entities;

import net.geocat.xml.helpers.CapabilitiesType;

import javax.persistence.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;

@Entity
@Table(name = "links")
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long linkId;

    @Column(columnDefinition = "text")
    //toString()
    private String summary;

    //link to harvest run by the harvester
    @Column(nullable = false,columnDefinition = "varchar(40)")
    private String HarvestJobId ;

    //link to the particular endpoint the service record (link) came from
    @Column(nullable = false)
    private long EndpointJobId;

     @Column(columnDefinition = "varchar(64)")
    //(link to actual service record text)
    private String OriginatingServiceRecordSHA2;

    @Column(columnDefinition = "text")
    //(from service record)
    private String OriginatingServiceRecordFileIdentifier;

    @Column(columnDefinition = "text")
     //(view, download, or discovery)
    private String OriginatingServiceRecordServiceType;

    @Column(columnDefinition = "text")
    // (WMS, WFS, WMTS, ATOM, WCS, or null)
    //Best guess by looking at the service record
    private String  OriginatingServiceRecordProtocolHint;

    @Column(columnDefinition = "text")
    // if link came from a containsOperation
    private String LinkOperationName;

    @Column(columnDefinition = "text")
    // from the CI_OnlineResource
    private String LinkProtocol;

    @Column(columnDefinition = "text")
    // from the CI_OnlineResource
    private String LinkFunction;

    @Column(columnDefinition = "text")
    // original link, but fixed (i.e. request=getmap to request=getcapabilities)
    private String  FixedLinkURL;

    @Column(columnDefinition = "text")
    // (original link's URL)
    private String  RawLinkURL;


    @Column(columnDefinition = "text")
    // i.e. connecttimeout
    private String  LinkHTTPException ;

    @Column(columnDefinition = "text")
    // i.e. connecttimeout
    private String  ResolveServiceMetadataLinkException;

    // (i.e. 200)
    private Integer  LinkHTTPStatusCode ;


    @Column(columnDefinition = "text")
    // (i.e. application/xml) from HTTP response
    private String  LinkMIMEType ;

    @Column(columnDefinition = "bytea")
    //(first 1000 bytes of request - might be able to determine what file type from this)
    private byte[] LinkContentHead ;

    // (is the link an XML document - i.e. starts with "<?xml")
    private Boolean LinkIsXML;

    @Enumerated(EnumType.STRING)
    // (WMS, WFS, WMTS, ATOM, WCS, or null) - by looking at the XML result
    private CapabilitiesType LinkCapabilitiesType;


    @Column(columnDefinition = "varchar(64)")
    //(link to another table with actual text XML data in it - only if LinkCapabilitiesType is not null
    private String     LinkContentSHA2 ;

    @Column(columnDefinition = "text")
    private String  ServiceMetadataLinkURL;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20)")
    private IndicatorStatus  Indicator_LinkResolves;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20)")
    private IndicatorStatus  Indicator_CapabilitiesResolves;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20)")
    private IndicatorStatus  Indicator_DetectProtocol;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20)")
    private IndicatorStatus  Indicator_HasExtendedCapabilities;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20)")
    private IndicatorStatus  Indicator_HasServiceMetadataLink;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20)")
    private IndicatorStatus  Indicator_ResolveServiceMetadataLink;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20)")
    private IndicatorStatus  Indicator_MetadataLinkIsXML;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20)")
    private IndicatorStatus  Indicator_MetadataLinkIsMD_METADATA;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20)")
    private IndicatorStatus  Indicator_MetadataLinkIsServiceRecord;

    @Column(columnDefinition = "text")
    // should be service
    private String  MetadataLinkMetadataType ;

    @Column(columnDefinition = "text")
    private String  MetadataLinkFileIdentifier ;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20)")
    private IndicatorStatus  Indicator_CompareServiceMetadataLink_Full;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20)")
    private IndicatorStatus  Indicator_CompareServiceMetadataLink_FileIdentifier;

    @Column(columnDefinition = "text")
    private String  MetadataRecordDifferences ;

    public long getLinkId() {
        return linkId;
    }

    public void setLinkId(long linkId) {
        this.linkId = linkId;
    }

    public String getHarvestJobId() {
        return HarvestJobId;
    }

    public void setHarvestJobId(String harvestJobId) {
        HarvestJobId = harvestJobId;
    }

    public long getEndpointJobId() {
        return EndpointJobId;
    }

    public void setEndpointJobId(long endpointJobId) {
        EndpointJobId = endpointJobId;
    }

    public String getOriginatingServiceRecordSHA2() {
        return OriginatingServiceRecordSHA2;
    }

    public void setOriginatingServiceRecordSHA2(String originatingServiceRecordSHA2) {
        OriginatingServiceRecordSHA2 = originatingServiceRecordSHA2;
    }

    public String getOriginatingServiceRecordFileIdentifier() {
        return OriginatingServiceRecordFileIdentifier;
    }

    public void setOriginatingServiceRecordFileIdentifier(String originatingServiceRecordFileIdentifier) {
        OriginatingServiceRecordFileIdentifier = originatingServiceRecordFileIdentifier;
    }

    public String getOriginatingServiceRecordServiceType() {
        return OriginatingServiceRecordServiceType;
    }

    public void setOriginatingServiceRecordServiceType(String originatingServiceRecordServiceType) {
        OriginatingServiceRecordServiceType = originatingServiceRecordServiceType;
    }

    public String getOriginatingServiceRecordProtocolHint() {
        return OriginatingServiceRecordProtocolHint;
    }

    public void setOriginatingServiceRecordProtocolHint(String originatingServiceRecordProtocolHint) {
        OriginatingServiceRecordProtocolHint = originatingServiceRecordProtocolHint;
    }

    public String getRawLinkURL() {
        return RawLinkURL;
    }

    public void setRawLinkURL(String linkURL) {
        RawLinkURL = linkURL;
    }

    public String getFixedLinkURL() {
        return FixedLinkURL;
    }

    public void setFixedLinkURL(String fixedLinkURL) {
        FixedLinkURL = fixedLinkURL;
    }

    public Integer getLinkHTTPStatusCode() {
        return LinkHTTPStatusCode;
    }

    public void setLinkHTTPStatusCode(Integer linkHTTPStatusCode) {
        LinkHTTPStatusCode = linkHTTPStatusCode;
    }

    public String getLinkMIMEType() {
        return LinkMIMEType;
    }

    public void setLinkMIMEType(String linkMIMEType) {
        LinkMIMEType = linkMIMEType;
    }

    public byte[] getLinkContentHead() {
        return LinkContentHead;
    }

    public void setLinkContentHead(byte[] linkContentHead) {
        LinkContentHead = linkContentHead;
    }

    public Boolean getLinkIsXML() {
        return LinkIsXML;
    }

    public void setLinkIsXML(Boolean linkIsXML) {
        LinkIsXML = linkIsXML;
    }

    public CapabilitiesType getLinkCapabilitiesType() {
        return LinkCapabilitiesType;
    }

    public void setLinkCapabilitiesType(CapabilitiesType linkCapabilitiesType) {
        LinkCapabilitiesType = linkCapabilitiesType;
    }

    public String getLinkContentSHA2() {
        return LinkContentSHA2;
    }

    public void setLinkContentSHA2(String linkContentSHA2) {
        LinkContentSHA2 = linkContentSHA2;
    }

    public String getServiceMetadataLinkURL() {
        return ServiceMetadataLinkURL;
    }

    public void setServiceMetadataLinkURL(String serviceMetadataLinkURL) {
        ServiceMetadataLinkURL = serviceMetadataLinkURL;
    }

    public IndicatorStatus getIndicator_LinkResolves() {
        return Indicator_LinkResolves;
    }

    public void setIndicator_LinkResolves(IndicatorStatus indicator_LinkResolves) {
        Indicator_LinkResolves = indicator_LinkResolves;
    }

    public IndicatorStatus getIndicator_CapabilitiesResolves() {
        return Indicator_CapabilitiesResolves;
    }

    public void setIndicator_CapabilitiesResolves(IndicatorStatus indicator_CapabilitiesResolves) {
        Indicator_CapabilitiesResolves = indicator_CapabilitiesResolves;
    }

    public IndicatorStatus getIndicator_DetectProtocol() {
        return Indicator_DetectProtocol;
    }

    public void setIndicator_DetectProtocol(IndicatorStatus indicator_DetectProtocol) {
        Indicator_DetectProtocol = indicator_DetectProtocol;
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

    public IndicatorStatus getIndicator_ResolveServiceMetadataLink() {
        return Indicator_ResolveServiceMetadataLink;
    }

    public void setIndicator_ResolveServiceMetadataLink(IndicatorStatus indicator_ResolveServiceMetadataLink) {
        Indicator_ResolveServiceMetadataLink = indicator_ResolveServiceMetadataLink;
    }

    public IndicatorStatus getIndicator_CompareServiceMetadataLink_Full() {
        return Indicator_CompareServiceMetadataLink_Full;
    }

    public void setIndicator_CompareServiceMetadataLink_Full(IndicatorStatus indicator_CompareServiceMetadataLink_Full) {
        Indicator_CompareServiceMetadataLink_Full = indicator_CompareServiceMetadataLink_Full;
    }

    public IndicatorStatus getIndicator_CompareServiceMetadataLink_FileIdentifier() {
        return Indicator_CompareServiceMetadataLink_FileIdentifier;
    }

    public void setIndicator_CompareServiceMetadataLink_FileIdentifier(IndicatorStatus indicator_CompareServiceMetadataLink_FileIdentifier) {
        Indicator_CompareServiceMetadataLink_FileIdentifier = indicator_CompareServiceMetadataLink_FileIdentifier;
    }

    public String getLinkOperationName() {
        return LinkOperationName;
    }

    public void setLinkOperationName(String linkOperationName) {
        LinkOperationName = linkOperationName;
    }

    public String getLinkProtocol() {
        return LinkProtocol;
    }

    public void setLinkProtocol(String linkProtocol) {
        LinkProtocol = linkProtocol;
    }

    public String getLinkFunction() {
        return LinkFunction;
    }

    public void setLinkFunction(String linkFunction) {
        LinkFunction = linkFunction;
    }

    public IndicatorStatus getIndicator_MetadataLinkIsXML() {
        return Indicator_MetadataLinkIsXML;
    }

    public void setIndicator_MetadataLinkIsXML(IndicatorStatus indicator_MetadataLinkIsXML) {
        Indicator_MetadataLinkIsXML = indicator_MetadataLinkIsXML;
    }

    public IndicatorStatus getIndicator_MetadataLinkIsMD_METADATA() {
        return Indicator_MetadataLinkIsMD_METADATA;
    }

    public void setIndicator_MetadataLinkIsMD_METADATA(IndicatorStatus indicator_MetadataLinkIsMD_METADATA) {
        Indicator_MetadataLinkIsMD_METADATA = indicator_MetadataLinkIsMD_METADATA;
    }

    public IndicatorStatus getIndicator_MetadataLinkIsServiceRecord() {
        return Indicator_MetadataLinkIsServiceRecord;
    }

    public void setIndicator_MetadataLinkIsServiceRecord(IndicatorStatus indicator_MetadataLinkIsServiceRecord) {
        Indicator_MetadataLinkIsServiceRecord = indicator_MetadataLinkIsServiceRecord;
    }

    public String getMetadataLinkMetadataType() {
        return MetadataLinkMetadataType;
    }

    public void setMetadataLinkMetadataType(String metadataLinkMetadataType) {
        MetadataLinkMetadataType = metadataLinkMetadataType;
    }

    public String getMetadataLinkFileIdentifier() {
        return MetadataLinkFileIdentifier;
    }

    public void setMetadataLinkFileIdentifier(String metadataLinkFileIdentifier) {
        MetadataLinkFileIdentifier = metadataLinkFileIdentifier;
    }

    public String getMetadataRecordDifferences() {
        return MetadataRecordDifferences;
    }

    public void setMetadataRecordDifferences(String metadataRecordDifferences) {
        MetadataRecordDifferences = metadataRecordDifferences;
    }

    public String getLinkHTTPException() {
        return LinkHTTPException;
    }

    public void setLinkHTTPException(String linkHTTPException) {
        LinkHTTPException = linkHTTPException;
    }

    public String getResolveServiceMetadataLinkException() {
        return ResolveServiceMetadataLinkException;
    }

    public void setResolveServiceMetadataLinkException(String resolveServiceMetadataLinkException) {
        ResolveServiceMetadataLinkException = resolveServiceMetadataLinkException;
    }

    @PreUpdate
    private void onUpdate() {
        this.summary = this.toString();
    }

    @PrePersist
    private void onInsert() {
        this.summary = this.toString();
    }


    @Override
    public String toString() {
        String result = "Link (id="+getLinkId()+")\n";
        result += "     +  Harvest Job: "+getHarvestJobId() +"\n";
        result += "     +  Endpoint Job: "+getEndpointJobId() +"\n";
        result += "     +  SHA2 of Service record this link came from: "+getOriginatingServiceRecordSHA2() +"\n";
        result += "     +  File Identifier of Service record this link came from: "+getOriginatingServiceRecordFileIdentifier() +"\n";
        result += "     +  Service Type of Service record this link came from: "+getOriginatingServiceRecordServiceType() +"\n";
        result += "     +  Protocol of Service record this link came from:  "+getOriginatingServiceRecordProtocolHint() +"\n";
        result += "     +  Original URL of Link: "+getRawLinkURL() +"\n";
        result += "     +  'Fixed' URL of Link: "+getFixedLinkURL() +"\n";

        result += "\n";

        if (getLinkHTTPException() != null)
            result += "     +  URL threw exception: "+getLinkHTTPException() +"\n";

        if (getLinkHTTPStatusCode() !=null)
             result += "     +  Status Code of HTTP request getting the link: "+getLinkHTTPStatusCode() +"\n";
        if (getLinkMIMEType() !=null)
            result += "     +  ContentType of HTTP request getting the link: "+getLinkMIMEType() +"\n";
        if (getLinkContentHead() != null) {
            result += "     +  Initial Data from request: " + Arrays.copyOf(getLinkContentHead(), 10) + "\n";
            result += "     +  Initial Data from request (text): " + new String(Arrays.copyOf(getLinkContentHead(), Math.min(100,getLinkContentHead().length))) + "\n";
        }
        if (getLinkIsXML() != null) {
            result += "     +  Link is XML: "+getLinkIsXML() +"\n";
        }
        if (getLinkCapabilitiesType() != null) {
            result += "     +  Link Capabilities Type: "+getLinkCapabilitiesType() +"\n";
        }
        if (getLinkContentSHA2() != null) {
            result += "     +  Link SHA2 of content: "+getLinkContentSHA2() +"\n";
        }
        if (getIndicator_LinkResolves() != null) {
            result += "     +  Indicator_LinkResolves: "+getIndicator_LinkResolves() +"\n";
        }
        if (getIndicator_CapabilitiesResolves() != null) {
            result += "     +  Indicator_CapabilitiesResolves: "+getIndicator_CapabilitiesResolves() +"\n";
        }
        if (getIndicator_DetectProtocol() != null) {
            result += "     +  Indicator_DetectProtocol: "+getIndicator_DetectProtocol() +"\n";
        }
        result += "\n";


        if (getIndicator_HasExtendedCapabilities() != null) {
            result += "     +  Indicator_HasExtendedCapabilities: "+getIndicator_HasExtendedCapabilities() +"\n";
        }
        if (getIndicator_HasServiceMetadataLink() != null) {
            result += "     +  Indicator_HasServiceMetadataLink: "+getIndicator_HasServiceMetadataLink() +"\n";
        }
        if (getServiceMetadataLinkURL() != null) {
            result += "     +  ServiceMetadataLinkURL: "+getServiceMetadataLinkURL() +"\n";
        }
        result += "\n";

        if (getIndicator_ResolveServiceMetadataLink() != null) {
            result += "     +  Indicator_ResolveServiceMetadataLink: "+getIndicator_ResolveServiceMetadataLink() +"\n";
        }

        if (getResolveServiceMetadataLinkException() != null) {
            result += "     +  ResolveServiceMetadataLinkException: "+getResolveServiceMetadataLinkException() +"\n";
        }


        if (getIndicator_MetadataLinkIsXML() != null) {
            result += "     +  Indicator_MetadataLinkIsXML: "+getIndicator_MetadataLinkIsXML() +"\n";
        }

        if (getIndicator_MetadataLinkIsMD_METADATA() != null) {
            result += "     +  Indicator_MetadataLinkIsMD_METADATA: "+getIndicator_MetadataLinkIsMD_METADATA() +"\n";
        }

        if (getMetadataLinkMetadataType() != null) {
            result += "     +  MetadataLinkMetadataType: "+getMetadataLinkMetadataType() +"\n";
        }

        if (getIndicator_MetadataLinkIsServiceRecord() != null) {
            result += "     +  Indicator_MetadataLinkIsServiceRecord: "+getIndicator_MetadataLinkIsServiceRecord() +"\n";
        }

        if (getMetadataLinkFileIdentifier() != null) {
            result += "     +  MetadataLinkFileIdentifier: "+getMetadataLinkFileIdentifier() +"\n";
        }

        if (getIndicator_CompareServiceMetadataLink_FileIdentifier() != null) {
            result += "     +  Indicator_CompareServiceMetadataLink_FileIdentifier: "+getIndicator_CompareServiceMetadataLink_FileIdentifier() +"\n";
        }
        if (getIndicator_CompareServiceMetadataLink_Full() != null) {
            result += "     +  Indicator_CompareServiceMetadataLink_Full: "+getIndicator_CompareServiceMetadataLink_Full() +"\n";
        }
        if (getMetadataRecordDifferences() != null) {
            result += "     +  MetadataRecordDifferences: "+getMetadataRecordDifferences() +"\n";
        }

        result += "=========================================================================";
        return result;
    }
}
