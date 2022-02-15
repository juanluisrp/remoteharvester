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

package net.geocat.service.html;

import net.geocat.database.linkchecker.entities.LinkCheckJob;
import net.geocat.database.linkchecker.entities.LocalDatasetMetadataRecord;
import net.geocat.database.linkchecker.entities.LocalServiceMetadataRecord;
import net.geocat.database.linkchecker.entities.SimpleLayerMetadataUrlDataLink;
import net.geocat.database.linkchecker.entities.SimpleStoredQueryDataLink;
import net.geocat.database.linkchecker.entities.helper.LinkToData;
import net.geocat.database.linkchecker.repos.LinkCheckJobRepo;
import net.geocat.database.linkchecker.repos.LocalDatasetMetadataRecordRepo;
import net.geocat.service.BlobStorageService;
import net.geocat.xml.XmlDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class HtmlDatasetService {

    @Autowired
    LinkCheckJobRepo linkCheckJobRepo;

    @Autowired
    BlobStorageService blobStorageService;

    @Autowired
    LocalDatasetMetadataRecordRepo localDatasetMetadataRecordRepo;

    public String lastLinkCheckJob(String country){
        if (country ==null)
            return lastLinkCheckJob();

        LinkCheckJob lastJob = null;
        for(LinkCheckJob job : linkCheckJobRepo.findAll()){
            if (!job.getLongTermTag().toLowerCase().startsWith(country.toLowerCase()))
                continue;
            if (lastJob == null)
                lastJob = job;
            if (lastJob.getCreateTimeUTC().compareTo(job.getCreateTimeUTC()) <1)
                lastJob = job;
        }
        return lastJob.getJobId();
    }


    public String lastLinkCheckJob(){
        LinkCheckJob lastJob = null;
        for(LinkCheckJob job : linkCheckJobRepo.findAll()){
            if (lastJob == null)
                lastJob = job;
            if (lastJob.getCreateTimeUTC().compareTo(job.getCreateTimeUTC()) <1)
                lastJob = job;
        }
        return lastJob.getJobId();
    }
    public String getHtml( String processID ,  String fileId) throws Exception {
        if ((processID == null) || (processID.trim().isEmpty()))
            processID = lastLinkCheckJob();
        processID = processID.trim();
        LocalDatasetMetadataRecord record = localDatasetMetadataRecordRepo.findFirstByFileIdentifierAndLinkCheckJobId(fileId, processID);
        if (record == null)
            return "<h1> Couldnt find Dataset record </h1>";

        String result = "<h1> Dataset Record</h1> \n";
        result += "<xmp>" + record.toString() + "</xmp><br>\n<br>\n";

        if (record.getDataLinks().size() == 0) {
            result += "NO links to data<Br><br>\n";
        }
        else {
            result += showDataLinks(record);
        }


        result += "<br><br><br><hr><br><br><xmp>"+text(record)+"</xmp><br><br>";
        return result;
    }

    private String showDataLinks(LocalDatasetMetadataRecord record) {
        String result = "";
        int indx =0;
        for (LinkToData link:record.getDataLinks()) {
            result += "<h3>link "+indx+" - " + link.getClass().getSimpleName() + "</h3>";
            result += "<a href='" +"/api/html/capabilities/"+ link.getLinkCheckJobId()+"/"+link.getCapabilitiesSha2() + "'>"+link.getCapabilitiesDocumentType()  + " Capabilities</a>" + "</h3>\n";

            if (link instanceof SimpleLayerMetadataUrlDataLink) {
                SimpleLayerMetadataUrlDataLink _link = (SimpleLayerMetadataUrlDataLink) link;
                result += "<br>ogcLayer: "+_link.getOgcLayerName()+"<br>\n";
            }
            if (link instanceof SimpleStoredQueryDataLink) {
                SimpleStoredQueryDataLink _link = (SimpleStoredQueryDataLink) link;
                result += "<br>storedProcName: "+_link.getStoredProcName()+"<br>\n";
                result += "code: "+_link.getCode()+"<br>\n";
                result += "codespace: "+_link.getCodeSpace()+"<br>\n";
            }
            indx++;
        }
        return result;
    }

    private String text(LocalDatasetMetadataRecord record) throws Exception {
        String xml = blobStorageService.findXML(record.getSha2());
        xml = XmlDoc.writeXMLPretty(xml);
        return xml;
    }
}
