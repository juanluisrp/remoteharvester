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

package net.geocat.database.linkchecker.entities.helper;

import net.geocat.xml.MetadataDocumentType;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class MetadataRecord {

    @Column(columnDefinition = "varchar(64)")
    private String sha2;

    @Column(columnDefinition = "text")
    private String fileIdentifier;

    @Column(columnDefinition = "text")
    //i.e. will be service
    private MetadataDocumentType metadataRecordType;

    //---------------------------------------------------------------------------

    public String getSha2() {
        return sha2;
    }

    public void setSha2(String sha2) {
        this.sha2 = sha2;
    }

    public String getFileIdentifier() {
        return fileIdentifier;
    }

    public void setFileIdentifier(String recordIdentifier) {
        this.fileIdentifier = recordIdentifier;
    }

    public MetadataDocumentType getMetadataRecordType() {
        return metadataRecordType;
    }

    public void setMetadataRecordType(MetadataDocumentType metadataRecordType) {
        this.metadataRecordType = metadataRecordType;
    }


    //---------------------------------------------------------------------------

    @Override
    public String toString() {
        String result = "";
        result += "     sha2: " + sha2 + "\n";
        result += "     fileIdentifier: " + fileIdentifier + "\n";
        result += "     metadataRecordType: " + metadataRecordType + "\n";
        return result;
    }
}
