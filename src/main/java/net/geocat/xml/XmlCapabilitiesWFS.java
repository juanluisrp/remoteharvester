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

package net.geocat.xml;

import net.geocat.xml.helpers.CapabilitiesType;
import org.w3c.dom.Node;

public class XmlCapabilitiesWFS extends XmlCapabilitiesDocument {

    private String SpatialDataSetIdentifier_metadataURL;
    private String SpatialDataSetIdentifier_Code;

    public XmlCapabilitiesWFS(XmlDoc doc) throws Exception {
        super(doc, CapabilitiesType.WFS);
        setup_XmlCapabilitiesWFS();
    }

    private void setup_XmlCapabilitiesWFS() throws Exception {
        Node sdi = xpath_node("//*[local-name()='ExtendedCapabilities']/inspire_dls:SpatialDataSetIdentifier");
        if (sdi != null) {
            SpatialDataSetIdentifier_metadataURL = this.xpath_attribute(sdi, ".", "metadataURL");
            Node code = xpath_node(sdi, "./inspire_common:Code");
            if (code != null)
                SpatialDataSetIdentifier_Code = code.getTextContent();
        }
    }


}