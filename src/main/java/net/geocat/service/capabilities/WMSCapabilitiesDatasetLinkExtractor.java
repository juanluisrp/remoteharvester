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

package net.geocat.service.capabilities;

import net.geocat.xml.XmlCapabilitiesDocument;
import net.geocat.xml.XmlDoc;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
@Scope("prototype")
public class WMSCapabilitiesDatasetLinkExtractor implements ICapabilitiesDatasetLinkExtractor {

    public String namespace="wms";
    public String namespaceIdentifier="wms";
    public String namespaceMetadataURL="wms";



    public static List<DatasetLink> unique(List<DatasetLink> list) {
        HashSet hs = new HashSet();
        hs.addAll(list);
        list.clear();
        list.addAll(hs);
        return list;
    }

    @Override
    public List<DatasetLink> findLinks(XmlCapabilitiesDocument doc) throws Exception {
        List<DatasetLink> result = new ArrayList<>();
        NodeList layers = doc.xpath_nodeset("//"+namespace+":Layer");
        for (int idx = 0; idx < layers.getLength(); idx++) {
            Node layer = layers.item(idx);
            DatasetLink link = processLayer(doc, layer);
            if (link != null)
                result.add(link);
        }

        return unique(result);
    }

    public DatasetLink processLayer(XmlCapabilitiesDocument doc, Node layer) throws Exception {
        String identifier = searchIdentifier(doc, layer);
        String metadataUrl = searchMetadataUrl(doc, layer);
        String authority = searchAuthority(doc,layer);

        if ((identifier != null) || (metadataUrl != null)) {
            DatasetLink result= new DatasetLink(identifier, metadataUrl);
            if ((authority != null) && (!authority.isEmpty()) )
                result.setAuthority(authority);
            return result;
        }
        return null;
    }

    protected String findMetadataURL(Node layer) throws Exception {
        Node n = XmlDoc.xpath_node(layer, "wms:MetadataURL/wms:OnlineResource");
        if (n == null)
            return null;
        Node att = n.getAttributes().getNamedItem("xlink:href");
        if (att == null)
            return null;
        return att.getTextContent();
    }


    private String searchMetadataUrl(XmlCapabilitiesDocument doc, Node layer) throws Exception {
        String metadataURL = findMetadataURL(layer);
        if ((metadataURL != null) && (!metadataURL.isEmpty()))
            return metadataURL;
        Node parentLayer = layer.getParentNode();
        if (parentLayer.getLocalName().equals("Layer"))
            return searchMetadataUrl(doc, parentLayer);
        return null;
    }

    private String findIdentifier(Node layer) throws Exception {
        Node n = XmlDoc.xpath_node(layer, namespaceIdentifier+":Identifier");
        if (n == null)
            return null;
        return n.getTextContent().trim();
    }


    public String searchIdentifier(XmlCapabilitiesDocument doc, Node layer) throws Exception {
        String localIdentifier = findIdentifier(layer);
        if ((localIdentifier != null) && (!localIdentifier.isEmpty()))
            return localIdentifier;
        Node parentLayer = layer.getParentNode();
        if (parentLayer.getLocalName().equals("Layer"))
            return searchIdentifier(doc, parentLayer);
        return null;
    }

    private String findAuthority(Node layer) throws Exception {
        Node n = XmlDoc.xpath_node(layer, namespace+":Identifier");
        if (n == null)
            return null;
        Node authorityNode = n.getAttributes().getNamedItem("authority");
        if (authorityNode == null)
            return null;
        String authority  = authorityNode.getNodeValue();
        if (!authority.isEmpty())
            return authority;
        return null;
    }

    public String searchAuthority(XmlCapabilitiesDocument doc, Node layer) throws Exception {
        String localAuthority = findAuthority(layer);
        if ((localAuthority != null) && (!localAuthority.isEmpty()))
            return localAuthority;
        Node parentLayer = layer.getParentNode();
        if (parentLayer.getLocalName().equals("Layer"))
            return searchAuthority(doc, parentLayer);
        return null;
    }

}
