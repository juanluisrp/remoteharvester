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

package net.geocat.simple;

import net.geocat.service.capabilities.WMSCapabilitiesDatasetLinkExtractor;
import net.geocat.service.capabilities.WMTSCapabilitiesDatasetLinkExtractor;
import net.geocat.xml.XmlCapabilitiesAtom;
import net.geocat.xml.XmlCapabilitiesWMTS;
import net.geocat.xml.XmlDocumentFactory;
import net.geocat.xml.helpers.CapabilityDeterminer;
import org.junit.Before;
import org.junit.Test;

import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestWMTSCapabilities {

    XmlDocumentFactory xmlDocumentFactory;
    WMTSCapabilitiesDatasetLinkExtractor wmtsCapabilitiesDatasetLinkExtractor;

    @Test
    public void testSimple() throws Exception {
        XmlCapabilitiesWMTS xmlCapabilitiesDocument = read("wmts_simple.xml");
        assertNotNull(xmlCapabilitiesDocument);

        assertEquals("serviceMetadataURL", xmlCapabilitiesDocument.getMetadataUrlRaw());


         assertEquals(1, xmlCapabilitiesDocument.getDatasetLinksList().size());

         assertEquals("layer1ID",xmlCapabilitiesDocument.getDatasetLinksList().get(0).getIdentifier());
         assertEquals("layerMetadataURL",xmlCapabilitiesDocument.getDatasetLinksList().get(0).getRawUrl());

    }


    public XmlCapabilitiesWMTS read(String fname) throws Exception {
        String text = new Scanner(TestWMSCapabilitiesDatasetLinkExtractor.class.getClassLoader().getResourceAsStream(fname), "UTF-8")
                .useDelimiter("\\A").next();
        return (XmlCapabilitiesWMTS)xmlDocumentFactory.create(text);
    }


    @Before
    public void setup(){
        xmlDocumentFactory = new XmlDocumentFactory();
        xmlDocumentFactory.capabilityDeterminer = new CapabilityDeterminer();
        wmtsCapabilitiesDatasetLinkExtractor = new WMTSCapabilitiesDatasetLinkExtractor();
    }
}
