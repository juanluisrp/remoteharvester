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

import net.geocat.database.linkchecker.entities.helper.PartialDownloadHint;
import net.geocat.database.linkchecker.entities.helper.RetrievableSimpleLink;
import net.geocat.database.linkchecker.entities.helper.ServiceMetadataRecord;

import javax.persistence.*;

@Entity
public class ServiceDocumentLink extends RetrievableSimpleLink {

    @Column(columnDefinition = "text")
    String operationName;
    @Column(columnDefinition = "text")
    String protocol;
    @Column(columnDefinition = "text")
    String function;
    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name="serviceMetadataId")
    ServiceMetadataRecord serviceMetadataRecord;
    @Column(columnDefinition = "text")
    String summary;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long serviceMetadataLinkId;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "capabilitiesDocumentId")
    private CapabilitiesDocument capabilitiesDocument;

    public ServiceDocumentLink() {
        this.setPartialDownloadHint(PartialDownloadHint.CAPABILITIES_ONLY);
    }


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

    public ServiceMetadataRecord getLocalServiceMetadataRecord() {
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
        result += "      serviceMetadataLinkId: " + serviceMetadataLinkId + "\n";
        if ((operationName != null) && (!operationName.isEmpty()))
            result += "      operationName: " + operationName + "\n";

        if ((protocol != null) && (!protocol.isEmpty()))
            result += "      protocol: " + protocol + "\n";
        if ((function != null) && (!function.isEmpty()))
            result += "      function: " + function + "\n";

//        if ( (serviceMetadataRecord != null)   )
//            result += "      serviceMetadataRecord record identifier: "+ serviceMetadataRecord.getFileIdentifier()+"\n";
//        if ( (serviceMetadataRecord != null)   )
//            result += "      serviceMetadataRecord Id: "+ serviceMetadataRecord.getServiceMetadataDocumentId()+"\n";

        result += "\n";
        result += super.toString();
        result += "\n";
        result += "     +  Link is Capabilities Document: " + (getCapabilitiesDocument() != null) + "\n";
//        if (getCapabilitiesDocument() != null) {
//            result += getCapabilitiesDocument().toString(8);
//        }

        result += "\n";
        result += "  }";
        return result;
    }
}
