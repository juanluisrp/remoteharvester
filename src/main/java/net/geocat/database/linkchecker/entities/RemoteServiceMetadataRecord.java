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

import net.geocat.database.linkchecker.entities.helper.ServiceMetadataRecord;
import net.geocat.database.linkchecker.entities.helper.IndicatorStatus;

import javax.persistence.*;

//represents a Service Metadata Record referenced by the Capabilities document
@Entity
@DiscriminatorValue("RemoteServiceMetadataRecord")
public class RemoteServiceMetadataRecord extends ServiceMetadataRecord {

    //which link was resolved to get this Service Metadata document
    @OneToOne(mappedBy = "remoteServiceMetadataRecord")
    private RemoteServiceMetadataRecordLink remoteServiceMetadataRecordLink;

    //summary info (for display)
    @Column(columnDefinition = "text")
    private String summary;

    //when comparing this to a local service record, do they have the same file identifiers?
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(5)")
    private IndicatorStatus Indicator_CompareServiceMetadataLink_FileIdentifier;

    //when comparing this to a local service record, are there no real XML differences?
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(5)")
    private IndicatorStatus Indicator_CompareServiceMetadataLink_Full;

    // cf. Indicator_CompareServiceMetadataLink_Full
    // this is a PARTIAL semi-human readable summary of xml difference
    @Column(columnDefinition = "text")
    private String metadataRecordDifferences;


    public RemoteServiceMetadataRecord(){
        super();
    }

    //---------------------------------------------------------------------------


    public IndicatorStatus getIndicator_CompareServiceMetadataLink_FileIdentifier() {
        return Indicator_CompareServiceMetadataLink_FileIdentifier;
    }

    public void setIndicator_CompareServiceMetadataLink_FileIdentifier(IndicatorStatus indicator_CompareServiceMetadataLink_FileIdentifier) {
        Indicator_CompareServiceMetadataLink_FileIdentifier = indicator_CompareServiceMetadataLink_FileIdentifier;
    }

    public IndicatorStatus getIndicator_CompareServiceMetadataLink_Full() {
        return Indicator_CompareServiceMetadataLink_Full;
    }

    public void setIndicator_CompareServiceMetadataLink_Full(IndicatorStatus indicator_CompareServiceMetadataLink_Full) {
        Indicator_CompareServiceMetadataLink_Full = indicator_CompareServiceMetadataLink_Full;
    }

    public String getMetadataRecordDifferences() {
        return metadataRecordDifferences;
    }

    public void setMetadataRecordDifferences(String metadataRecordDifferences) {
        this.metadataRecordDifferences = metadataRecordDifferences;
    }

    public RemoteServiceMetadataRecordLink getRemoteServiceMetadataRecordLink() {
        return remoteServiceMetadataRecordLink;
    }

    public void setRemoteServiceMetadataRecordLink(RemoteServiceMetadataRecordLink remoteServiceMetadataRecordLink) {
        this.remoteServiceMetadataRecordLink = remoteServiceMetadataRecordLink;
    }


    //---------------------------------------------------------------------------

    @PreUpdate
    protected void onUpdate() {
        super.onUpdate();
        summary = toString();
    }

    @PrePersist
    protected void onInsert() {
        super.onInsert();
        summary = toString();
    }

    //---------------------------------------------------------------------------

    @Override
    public String toString() {
        String result = "RemoteServiceMetadataRecord {\n";
        result += "     serviceMetadataDocumentId: " + getServiceMetadataDocumentId() + "\n";

        result += super.toString();

        result += "\n";

        result += " }";
        return result;
    }
}
