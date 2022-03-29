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

package net.geocat.eventprocessor.processors.datadownload.downloaders;

import net.geocat.database.linkchecker.entities.SimpleAtomLinkToData;
import net.geocat.database.linkchecker.entities.helper.AtomSubFeedRequest;
import net.geocat.xml.XmlCapabilitiesAtom;
import net.geocat.xml.XmlDoc;
import net.geocat.xml.XmlDocumentFactory;
import net.geocat.xml.XmlStringTools;
import net.geocat.xml.helpers.AtomEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class AtomDownloadProcessor {

    private static final Logger logger = LoggerFactory.getLogger( AtomDownloadProcessor.class);

    @Autowired
    AtomLayerDownloader atomLayerDownloader;

    @Autowired
    XmlDocumentFactory xmlDocumentFactory;

    public void process(SimpleAtomLinkToData simpleAtomLinkToData,OGCInfoCacheItem ogcInfoCacheItem) throws Exception {
        if ( (simpleAtomLinkToData ==null) || (ogcInfoCacheItem ==null))
            return;

        AtomSubFeedRequest subFeedRequest= atomLayerDownloader.createSubFeedRequest((XmlCapabilitiesAtom)ogcInfoCacheItem.getXmlCapabilitiesDocument(), simpleAtomLinkToData.getLayerId());
        simpleAtomLinkToData.setAtomSubFeedRequest(subFeedRequest);

        atomLayerDownloader.resolve(subFeedRequest);
        atomLayerDownloader.validate(subFeedRequest);
        if (!subFeedRequest.getSuccessfulAtomRequest()) {
            simpleAtomLinkToData.setSuccessfullyDownloaded(false);
            return;
        }
        String xmlAtomFeed2 = XmlStringTools.bytea2String(subFeedRequest.getFullData());
        XmlDoc doc =  xmlDocumentFactory.create(xmlAtomFeed2);
        if (!(doc instanceof  XmlCapabilitiesAtom)) {
            simpleAtomLinkToData.setSuccessfullyDownloaded(false);
            return;
        }
        XmlCapabilitiesAtom secondaryAtom = (XmlCapabilitiesAtom) doc;
        if (secondaryAtom.getEntries().isEmpty()) {
            simpleAtomLinkToData.setSuccessfullyDownloaded(false);
            return;
        }

        //okay, 2nd phase -- try to find one dataset that fully downloads!
        for (AtomEntry entry : secondaryAtom.getEntries()) {

        }
    }

}
