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

package net.geocat.database.linkchecker.entities2;

import javax.persistence.*;

@Entity
public class MetadataDocument {

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20)")
    MetadataDocumentState state;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long metadataDocumentId;
    @Column(columnDefinition = "varchar(40)")
    private String linkCheckJobId;
    @Column(columnDefinition = "varchar(64)")
    private String sha2;
    @Column(columnDefinition = "text")
    private String recordIdentifier;
    private long harvesterMetadataRecordId;
    @Column(columnDefinition = "text")
    //i.e. service/dataset
    private String metadataRecordType;
    @Column(columnDefinition = "text")
    //i.e. view/download/discovery
    private String metadataServiceType;
    private Integer numberOfLinksFound;
    private Integer numberOfOperatesOnFound;

    public long getMetadataDocumentId() {
        return metadataDocumentId;
    }

    public void setMetadataDocumentId(long metadataDocumentId) {
        this.metadataDocumentId = metadataDocumentId;
    }

    public String getLinkCheckJobId() {
        return linkCheckJobId;
    }

    public void setLinkCheckJobId(String linkCheckJobId) {
        this.linkCheckJobId = linkCheckJobId;
    }

    public String getSha2() {
        return sha2;
    }

    public void setSha2(String sha2) {
        this.sha2 = sha2;
    }

    public String getRecordIdentifier() {
        return recordIdentifier;
    }

    public void setRecordIdentifier(String recordIdentifier) {
        this.recordIdentifier = recordIdentifier;
    }

    public long getHarvesterMetadataRecordId() {
        return harvesterMetadataRecordId;
    }

    public void setHarvesterMetadataRecordId(long harvesterMetadataRecordId) {
        this.harvesterMetadataRecordId = harvesterMetadataRecordId;
    }

    public String getMetadataRecordType() {
        return metadataRecordType;
    }

    public void setMetadataRecordType(String metadataRecordType) {
        this.metadataRecordType = metadataRecordType;
    }

    public MetadataDocumentState getState() {
        return state;
    }

    public void setState(MetadataDocumentState state) {
        this.state = state;
    }

    public String getMetadataServiceType() {
        return metadataServiceType;
    }

    public void setMetadataServiceType(String metadataServiceType) {
        this.metadataServiceType = metadataServiceType;
    }


    public Integer getNumberOfLinksFound() {
        return numberOfLinksFound;
    }

    public void setNumberOfLinksFound(Integer numberOfLinksFound) {
        this.numberOfLinksFound = numberOfLinksFound;
    }

    public Integer getNumberOfOperatesOnFound() {
        return numberOfOperatesOnFound;
    }

    public void setNumberOfOperatesOnFound(Integer numberOfOperatesOnFound) {
        this.numberOfOperatesOnFound = numberOfOperatesOnFound;
    }

    @Override
    public String toString() {
        String result = "MetadataDocument {\n";
        result += "     metadataDocumentId:" + metadataDocumentId + "\n";
        result += "     linkCheckJobId:" + linkCheckJobId + "\n";
        result += "     sha2:" + sha2 + "\n";
        result += "     harvesterMetadataRecordId:" + harvesterMetadataRecordId + "\n";
        result += "     state:" + state + "\n";

        result += "     recordIdentifier:" + recordIdentifier + "\n";
        result += "     metadataRecordType:" + metadataRecordType + "\n";
        result += "     metadataServiceType:" + metadataServiceType + "\n";
        if (numberOfLinksFound != null)
            result += "     numberOfLinksFound:" + numberOfLinksFound + "\n";
        if (numberOfOperatesOnFound != null)
            result += "     numberOfOperatesOnFound:" + numberOfOperatesOnFound + "\n";

        result += "}";
        return result;
    }
}
