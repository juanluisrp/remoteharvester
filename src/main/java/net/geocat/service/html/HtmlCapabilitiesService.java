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

import net.geocat.database.linkchecker.entities.CapabilitiesDatasetMetadataLink;
import net.geocat.database.linkchecker.entities.CapabilitiesDocument;
import net.geocat.database.linkchecker.entities.InspireSpatialDatasetIdentifier;
import net.geocat.database.linkchecker.entities.LinkCheckJob;
import net.geocat.database.linkchecker.entities.LocalServiceMetadataRecord;
import net.geocat.database.linkchecker.entities.helper.SHA2JobIdCompositeKey;
import net.geocat.database.linkchecker.repos.CapabilitiesDocumentRepo;
import net.geocat.database.linkchecker.repos.LinkCheckBlobStorageRepo;
import net.geocat.database.linkchecker.repos.LinkCheckJobRepo;
import net.geocat.xml.XmlDoc;
import net.geocat.xml.XmlDocumentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HtmlCapabilitiesService {

    @Autowired
    LinkCheckBlobStorageRepo linkCheckBlobStorageRepo;


    @Autowired
    LinkCheckJobRepo linkCheckJobRepo;

    @Autowired
    CapabilitiesDocumentRepo capabilitiesDocumentRepo;

    @Autowired
    XmlDocumentFactory xmlDocumentFactory;

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

    public String getHtml( String processID ,  String sha2) throws Exception {
        if ((processID ==null) || (processID.trim().isEmpty()))
            processID = lastLinkCheckJob();
        processID = processID.trim();
        CapabilitiesDocument capabilitiesDocument= capabilitiesDocumentRepo.findById( new SHA2JobIdCompositeKey(sha2,processID)).get();
        if (capabilitiesDocument == null)
            return "<h1> Couldnt find capabilitiesDocument record </h1>";
        String result =  "<h1> Capabilities Document  </h1> \n";
        result += "<xmp>"+capabilitiesDocument.toString()  + "</xmp><br>\n<br>\n";


        if (capabilitiesDocument.getInspireSpatialDatasetIdentifiers().isEmpty())
            result += "NO Inspire Spatial Dataset Identifiers<br>";
        for (InspireSpatialDatasetIdentifier id:capabilitiesDocument.getInspireSpatialDatasetIdentifiers()) {
            result += id.toString() +"<br>\n";
        }
        result+="<br>";

        if (capabilitiesDocument.getRemoteServiceMetadataRecordLink() == null){
            result += "NO Link to Service Metadata record<br>";
        }
        else {
            result += "<xmp>"+ capabilitiesDocument.getRemoteServiceMetadataRecordLink().toString()+"</xmp><br>\n";
        }

        result += "<br> <h3>Layers</h3><br>";
        int idx =0;
        for(CapabilitiesDatasetMetadataLink layer : capabilitiesDocument.getCapabilitiesDatasetMetadataLinkList()) {
            result += "<h3> layer #"+idx+"</h3><br>\n";
            result += "fully downloaded: "+layer.getUrlFullyRead() +"<br><br>";
            result += "<xmp>"+ layer.toString() + "</xmp><br>\n";
            idx++;
        }


        result += "<br><br><br><hr><br><br><xmp>"+text(capabilitiesDocument)+"</xmp><br><br>";
        return result;
     }




    private String text(CapabilitiesDocument cap) throws Exception {
        String xml =linkCheckBlobStorageRepo.findById(cap.getSha2()).get().getTextValue() ;
        XmlDoc x =  xmlDocumentFactory.create(xml);
        xml = XmlDoc.writeXMLPretty(xml);
        return xml;
    }
}
