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

import net.geocat.database.linkchecker.entities.CapabilitiesDocument;
import net.geocat.database.linkchecker.entities.OGCRequest;
import net.geocat.http.AlwaysAbortContinueReadingPredicate;
import net.geocat.http.IHTTPRetriever;
import net.geocat.service.downloadhelpers.PartialDownloadPredicateFactory;
import net.geocat.service.downloadhelpers.RetrievableSimpleLinkDownloader;
import net.geocat.xml.XmlCapabilitiesWFS;
import net.geocat.xml.XmlCapabilitiesWMS;
import net.geocat.xml.XmlStringTools;
import net.geocat.xml.helpers.WMSLayer;
import net.geocat.xml.helpers.WMSLayerBBox;
import net.geocat.xml.helpers.XmlTagInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static net.geocat.eventprocessor.processors.datadownload.downloaders.DownloaderHelper.fixBaseURL;
import static net.geocat.eventprocessor.processors.datadownload.downloaders.DownloaderHelper.isRecognizedImage;
import static net.geocat.eventprocessor.processors.datadownload.downloaders.DownloaderHelper.isSame;
import static net.geocat.eventprocessor.processors.datadownload.downloaders.DownloaderHelper.setParameter;
import static net.geocat.xml.XmlStringTools.determineRootTagInfo;



@Component
@Scope("prototype")
public class WMSLayerDownloader {


    private static final Logger logger = LoggerFactory.getLogger(net.geocat.eventprocessor.processors.datadownload.downloaders.WMSLayerDownloader.class);


    @Autowired
    @Qualifier("cachingHttpRetriever")
    IHTTPRetriever retriever;

    @Autowired
    PartialDownloadPredicateFactory partialDownloadPredicateFactory;

    @Autowired
    AlwaysAbortContinueReadingPredicate alwaysAbortContinueReadingPredicate;

    @Autowired
    RetrievableSimpleLinkDownloader retrievableSimpleLinkDownloader;


    public static String addBasicItemsToUrl(String baseUrl,String wmsVersion) throws Exception {
        String url = baseUrl;
        url = setParameter(url, "REQUEST", "GetMap");
        url = setParameter(url, "SERVICE", "WMS");
        url = setParameter(url, "VERSION", wmsVersion);
        return url;
    }



    public String determineSRSParam(XmlCapabilitiesWMS wmsCap) {
        if ( (wmsCap.getVersionNumber() == null) || (wmsCap.getVersionNumber().isEmpty()))
            return "CRS";
        if (wmsCap.getVersionNumber().startsWith("1.3"))
            return "SRS";
        return  "CRS";
    }

    public String findBestImageFormat(XmlCapabilitiesWMS wmsCap) throws Exception {
        if (wmsCap.supportsFormat("image/png"))
            return "image/png";
        if (wmsCap.supportsFormat("image/jpeg"))
            return "image/jpeg";
        if (wmsCap.supportsFormat("image/jpg"))
            return "image/jpg";
        throw new Exception("couldnt find an appropriate WMS image format");
    }

    public String createURL(XmlCapabilitiesWMS wmsCap, String layerName) throws Exception {
        String url = fixBaseURL(wmsCap.getGetMapEndpoint());
        url = addBasicItemsToUrl(url, wmsCap.getVersionNumber());
        url = setParameter(url, "LAYERS", layerName);

        String bestFormat = findBestImageFormat(wmsCap);
        url = setParameter(url,"FORMAT",bestFormat);

        WMSLayer layer = wmsCap.findWMSLayer(layerName);
        if (layer == null)
            throw new Exception("wms doesn't know about layer:"+layerName);
        if ( (layer.getWmsLayerBBoxList() == null) || (layer.getWmsLayerBBoxList().isEmpty()))
            throw new Exception("couldnt extra bounds for layer:"+layerName);

        WMSLayerBBox wmsLayerBBox = layer.getWmsLayerBBoxList().get(0);

        url = setParameter(url, determineSRSParam(wmsCap), wmsLayerBBox.getCRS());
        url = setParameter(url,"BBOX", wmsLayerBBox.asBBOX());

        url = setParameter(url,"HEIGHT", "256");
        url = setParameter(url,"WIDTH", "256");
        return url;
    }



    public OGCRequest downloads(XmlCapabilitiesWMS wmsCap, String layerName) throws Exception {
        String url = createURL(wmsCap,layerName);

        OGCRequest ogcRequest = new OGCRequest(url);
        retrievableSimpleLinkDownloader.process(ogcRequest, 4096);

        if (ogcRequest.getLinkHTTPStatusCode() != 200) {
            ogcRequest.setSuccessfulOGCRequest(false);
            ogcRequest.setUnSuccessfulOGCRequestReason("http result code is not 200");
            return ogcRequest;
        }

        if (!isRecognizedImage(ogcRequest.getLinkContentHead())) {
            ogcRequest.setUnSuccessfulOGCRequestReason("http result is not a recognized image type (png or jpeg)");
            ogcRequest.setSuccessfulOGCRequest(false);
            return ogcRequest;
        }



        ogcRequest.setSuccessfulOGCRequest(true);
        return ogcRequest;
    }

    public boolean downloads(CapabilitiesDocument wmsCap, String layerName) throws Exception {


        return false;
    }

}


