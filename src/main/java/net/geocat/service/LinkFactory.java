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

package net.geocat.service;

import net.geocat.database.linkchecker.entities2.Link;
import net.geocat.database.linkchecker.entities2.LinkState;
import net.geocat.service.capabilities.CapabilitiesLinkFixer;
import net.geocat.xml.XmlServiceRecordDoc;
import net.geocat.xml.helpers.OnlineResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LinkFactory {

    @Autowired
    CapabilitiesLinkFixer capabilitiesLinkFixer;

    public Link create(OnlineResource onlineResource,
                       XmlServiceRecordDoc doc,
                       String sha2,
                       String harvestId,
                       long endpointJobId,
                       String linkCheckJobId)
            throws Exception {
        Link result = new Link();

        result.setHarvestJobId(harvestId);
        result.setEndpointJobId(endpointJobId);
        result.setOriginatingServiceRecordSHA2(sha2);

        result.setOriginatingServiceRecordFileIdentifier(doc.getFileIdentifier());
        result.setOriginatingServiceRecordServiceType(doc.getServiceType());
        result.setRawLinkURL(onlineResource.getRawURL());
        result.setFixedLinkURL(capabilitiesLinkFixer.fix(onlineResource.getRawURL()));
        result.setLinkProtocol(onlineResource.getProtocol());
        result.setLinkOperationName(onlineResource.getOperationName());
        result.setLinkFunction(onlineResource.getFunction());
        result.setLinkCheckJobId(linkCheckJobId);

        result.setLinkState(LinkState.CREATED);

        return result;
    }
}
