package net.geocat.database.linkchecker.entities;

import net.geocat.xml.helpers.CapabilitiesType;

import javax.persistence.*;

@Entity
@Table(name = "links")
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long linkId;


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
    // (actual link's URL)
    private String  LinkURL;


    @Column(columnDefinition = "text")
    // (i.e. 200)
    private String  LinkHTTPStatusCode ;


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
    private IndicatorStatus  ServiceMetadataLinkURL;

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
    private IndicatorStatus  Indicator_CompareServiceMetadataLink;

}
