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

package com.geocat.ingester.model.linkchecker;

import com.geocat.ingester.model.linkchecker.helper.PartialDownloadHint;
import com.geocat.ingester.model.linkchecker.helper.RetrievableSimpleLink;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Entity
public class RemoteServiceMetadataRecordLink extends RetrievableSimpleLink {

    //    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
//    @JoinColumn(name = "capabilitiesDocumentId" )
    @OneToOne(mappedBy = "remoteServiceMetadataRecordLink")
    CapabilitiesDocument capabilitiesDocument;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "remoteServiceMetadataRecordId")
    RemoteServiceMetadataRecord remoteServiceMetadataRecord;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long remoteServiceMetadataRecordLinkId;
    @Column(columnDefinition = "text")
    private String summary;

    public RemoteServiceMetadataRecordLink() {
        this.setPartialDownloadHint(PartialDownloadHint.METADATA_ONLY);
    }

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
        String result = "RemoteServiceMetadataRecordLink {\n";
        result += "      remoteServiceMetadataRecordLinkId: " + remoteServiceMetadataRecordLinkId + "\n";


        result += "\n";
        result += super.toString();
        result += "\n";


        result += "      has remote Service Metadata Record :" + (remoteServiceMetadataRecord != null) + "\n";
        if (remoteServiceMetadataRecord != null) {
            result += "      Remote Service Metadata Record file identifier:" + remoteServiceMetadataRecord.getFileIdentifier() + "\n";
        }
        result += "  }";
        return result;
    }
}
