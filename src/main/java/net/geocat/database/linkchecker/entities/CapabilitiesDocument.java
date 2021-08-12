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

package net.geocat.database.linkchecker.entities;

import net.geocat.database.linkchecker.entities.helper.UpdateCreateDateTimeEntity;
import net.geocat.database.linkchecker.entities2.IndicatorStatus;
import net.geocat.xml.helpers.CapabilitiesType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class CapabilitiesDocument extends UpdateCreateDateTimeEntity {


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
    @OneToOne(mappedBy = "capabilitiesDocument",cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private ServiceDocumentLink serviceDocumentLink;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20)")
    private IndicatorStatus Indicator_HasExtendedCapabilities;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20)")
    private IndicatorStatus Indicator_HasServiceMetadataLink;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    @JoinColumn(name = "capabilitiesDocumentId")
    private RemoteServiceMetadataRecordLink remoteServiceMetadataRecordLink;


    @OneToMany(mappedBy = "capabilitiesDocument",
            cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<CapabilitiesDatasetMetadataLink> capabilitiesDatasetMetadataLinkList;


    @Column(columnDefinition = "text")
    private String summary;


    public CapabilitiesDocument(){
        this.capabilitiesDatasetMetadataLinkList = new ArrayList<>();
    }

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
    protected void onUpdate() {
        super.onUpdate();
        this.summary = this.toString();
    }

    @PrePersist
    protected void onInsert() {
        super.onInsert();
        this.summary = this.toString();
    }

    //---------------------------------------------------------------------------

    @Override
    public String toString() {
        return toString(0);
    }

    public String toString(int indentSpaces) {
        String indent = "                                                     ".substring(0, indentSpaces);
        String result = indent + "CapabilitiesDocument {\n";
        result += indent + "      capabilitiesDocumentId: " + capabilitiesDocumentId + "\n";

        result+= super.toString();

        if ((sha2 != null) && (!sha2.isEmpty()))
            result += indent + "      sha2: " + sha2 + "\n";
        if (capabilitiesDocumentType != null)
            result += indent + "      capabilitiesDocumentType: " + capabilitiesDocumentType + "\n";

        if (Indicator_HasExtendedCapabilities != null)
            result += indent + "      Indicator_HasExtendedCapabilities: " + Indicator_HasExtendedCapabilities + "\n";
        if (Indicator_HasServiceMetadataLink != null)
            result += indent + "      Indicator_HasServiceMetadataLink: " + Indicator_HasServiceMetadataLink + "\n";


//        if ( (serviceDocumentLink != null)   )
//            result += indent+"      serviceDocumentLink Id: "+serviceDocumentLink.getServiceMetadataLinkId()+"\n";

        if (remoteServiceMetadataRecordLink != null) {
            result += indent + "      has Remote Service Metadata link: true\n";
            result += indent + "      Remote Service Metadata URL: " + remoteServiceMetadataRecordLink.getRawURL() + "\n";
        }

        result += indent + "  }";
        return result;
    }

}
