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

import net.geocat.database.linkchecker.entities.OperatesOnLink;
import net.geocat.database.linkchecker.entities.ServiceDocumentLink;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "service_record_type",
        discriminatorType = DiscriminatorType.STRING)
public class ServiceMetadataRecord extends MetadataRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long serviceMetadataDocumentId;

    @Column(columnDefinition = "text")
    //i.e. view/download/discovery
    private String metadataServiceType;

    private Integer numberOfLinksFound;
    private Integer numberOfOperatesOnFound;

    @OneToMany(mappedBy = "serviceMetadataRecord",
            cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    // @JoinColumn(name="serviceMetadataRecordId")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<ServiceDocumentLink> serviceDocumentLinks;

    @OneToMany(mappedBy = "serviceMetadataRecord",
            cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    // @JoinColumn(name="serviceMetadataRecordId")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<OperatesOnLink> operatesOnLinks;


    //---------------------------------------------------------------------------


    public long getServiceMetadataDocumentId() {
        return serviceMetadataDocumentId;
    }

    public void setServiceMetadataDocumentId(long serviceMetadataDocumentId) {
        this.serviceMetadataDocumentId = serviceMetadataDocumentId;
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

    public List<ServiceDocumentLink> getServiceDocumentLinks() {
        return serviceDocumentLinks;
    }

    public void setServiceDocumentLinks(List<ServiceDocumentLink> serviceDocumentLinks) {
        this.serviceDocumentLinks = serviceDocumentLinks;
    }

    public List<OperatesOnLink> getOperatesOnLinks() {
        return operatesOnLinks;
    }

    public void setOperatesOnLinks(List<OperatesOnLink> operatesOnLinks) {
        this.operatesOnLinks = operatesOnLinks;
    }


    //---------------------------------------------------------------------------


    protected void onUpdate() {
        update();
    }


    protected void onInsert() {
        update();
    }

    protected void update() {
        if (serviceDocumentLinks != null)
            numberOfLinksFound = serviceDocumentLinks.size();
        if (operatesOnLinks != null)
            numberOfOperatesOnFound = operatesOnLinks.size();
    }

    //---------------------------------------------------------------------------

    @Override
    public String toString() {
        update();
        String result = super.toString();

        result += "     metadataServiceType: " + metadataServiceType + "\n";
        if (numberOfLinksFound != null)
            result += "     numberOfLinksFound: " + numberOfLinksFound + "\n";
        if (numberOfOperatesOnFound != null)
            result += "     numberOfOperatesOnFound: " + numberOfOperatesOnFound + "\n";

        return result;
    }
}
