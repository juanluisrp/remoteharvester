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

import net.geocat.service.capabilities.DatasetLink;
import net.geocat.service.capabilities.WMSCapabilitiesDatasetLinkExtractor;
import net.geocat.xml.helpers.CapabilitiesType;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathExpressionException;

public class XmlCapabilitiesWFS extends XmlCapabilitiesDocument {



    public XmlCapabilitiesWFS(XmlDoc doc) throws Exception {
        super(doc, CapabilitiesType.WFS);
        setup_XmlCapabilitiesWFS();
    }

    private String getLayerMetadataURL() throws  Exception {
        NodeList nl = xpath_nodeset("//wfs:FeatureType/wfs:MetadataURL/@xlink:href");
        for(int idx=0;idx<nl.getLength();idx++){
            Node n = nl.item(idx);
            String value = n.getNodeValue();
            if  (  (value == null) || ( value.isEmpty()) )
                continue;
            return value.trim();
        }
        return null;
    }

    private void setup_XmlCapabilitiesWFS() throws Exception {
        NodeList sdi = xpath_nodeset("//inspire_dls:SpatialDataSetIdentifier");


        for(int idx=0; idx<sdi.getLength();idx++){
            Node n = sdi.item(idx);
            String url =getLayerMetadataURL();

            Node codeNode = xpath_node(n,"./inspire_common:Code");
            String identity = null;
            if (codeNode != null)
                identity = codeNode.getTextContent();
            if ( (url != null) || (identity !=null)){
                DatasetLink datasetLink = new DatasetLink(identity,url);
                datasetLinksList.add(datasetLink);
            }
        }
        setup_XmlCapabilitiesWFS_noinspire();
    }

    //alternative way - direct links
    private void setup_XmlCapabilitiesWFS_noinspire() throws Exception {
        NodeList metaurls = xpath_nodeset("//wfs:FeatureTypeList/wfs:FeatureType/wfs:MetadataURL");
        for(int idx=0; idx<metaurls.getLength();idx++) {
            Node n = metaurls.item(idx);
            Node urlNode = n.getAttributes().getNamedItem("xlink:href");
            if ( (urlNode != null) && (!urlNode.getNodeValue().isEmpty()) ) {
                String url = urlNode.getNodeValue();
                DatasetLink datasetLink = new DatasetLink(null,url);
                datasetLinksList.add(datasetLink);
            }
        }
        datasetLinksList = WMSCapabilitiesDatasetLinkExtractor.unique(datasetLinksList);
    }

    @Override
    public String toString() {
        String result =  "XmlCapabilitiesWFS(has service reference URL="+( (getMetadataUrlRaw() !=null) && (!getMetadataUrlRaw().isEmpty())) ;
        result += ", number of Dataset links = "+getDatasetLinksList().size();
        result += ")";
        return result;
    }
}