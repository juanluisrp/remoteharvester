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
public class OperatesOnLink extends RetrievableSimpleLink {

    @Column(columnDefinition = "text")
    String uuidref;
    @Column(columnDefinition = "text")
    String summary;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long operatesOnLinkId;
    @ManyToOne(fetch = FetchType.EAGER)
    //@JoinColumn(name="serviceMetadataId")
    private ServiceMetadataRecord serviceMetadataRecord;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "datasetMetadataRecordId")
    private OperatesOnRemoteDatasetMetadataRecord datasetMetadataRecord;


    public OperatesOnLink() {
        this.setPartialDownloadHint(PartialDownloadHint.METADATA_ONLY);
    }


    //---------------------------------------------------------------------------

    public ServiceMetadataRecord getServiceMetadataRecord() {
        return serviceMetadataRecord;
    }

    public void setServiceMetadataRecord(ServiceMetadataRecord serviceMetadataRecord) {
        this.serviceMetadataRecord = serviceMetadataRecord;
    }

    public long getOperatesOnLinkId() {
        return operatesOnLinkId;
    }

    public void setOperatesOnLinkId(long operatesOnLinkId) {
        this.operatesOnLinkId = operatesOnLinkId;
    }

    public String getUuidref() {
        return uuidref;
    }

    public void setUuidref(String uuidref) {
        this.uuidref = uuidref;
    }


    public OperatesOnRemoteDatasetMetadataRecord getDatasetMetadataRecord() {
        return datasetMetadataRecord;
    }

    public void setDatasetMetadataRecord(OperatesOnRemoteDatasetMetadataRecord datasetMetadataRecord) {
        this.datasetMetadataRecord = datasetMetadataRecord;
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
        String result = "OperatesOnLink {\n";
        result += "      operatesOnLinkId: " + operatesOnLinkId + "\n";
        if ((uuidref != null) && (!uuidref.isEmpty()))
            result += "      uuidref: " + uuidref + "\n";


        result += super.toString();
        result += "      has dataset Metadata Record :" + (datasetMetadataRecord != null) + "\n";
        if (datasetMetadataRecord != null) {
            result += "      dataset Metadata Record file identifier: " + datasetMetadataRecord.getFileIdentifier() + "\n";
            result += "      dataset Metadata Record dataset identifier: " + datasetMetadataRecord.getDatasetIdentifier() + "\n";
        }
//        if ( (serviceMetadataRecord != null)   )
//            result += "      serviceMetadataRecord record identifier: "+ serviceMetadataRecord.getFileIdentifier()+"\n";
//        if ( (localServiceMetadataRecord != null)   )
//            result += "      serviceMetadataRecord Id: "+ sServiceMetadataRecord.getServiceMetadataDocumentId()+"\n";

        result += "  }";
        return result;
    }

}
