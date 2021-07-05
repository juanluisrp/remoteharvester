package net.geocat.xml.helpers;

import net.geocat.xml.XmlDoc;
import net.geocat.xml.XmlServiceRecordDoc;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathExpressionException;
import java.net.MalformedURLException;
import java.net.URL;

@Component
public class DownloadServiceTypeProbe {

    public DownloadServiceType determineSpecificServiceType(XmlServiceRecordDoc doc) throws Exception {
        if (!doc.getServiceType().equals("download"))
            throw new Exception("should be called with a 'download' service type - this is "+doc.getServiceType());
//        if ( (doc.getContainsOperationsGetCapabilitiesNodeList() == null) || (doc.getContainsOperationsGetCapabilitiesNodeList().getLength()==0) )
//            throw new  Exception("should be called with a document with containsOperation/operationName = 'GetCapabilities' ");
//
//        for(int idx=0;idx<doc.getContainsOperationsGetCapabilitiesNodeList().getLength();idx++)
//        {
//            Node containsOpNode = doc.getContainsOperationsGetCapabilitiesNodeList().item(idx);
//            String url = findURL(containsOpNode);
//            return probeURL(url);
//        }

        return DownloadServiceType.UNKNOWN;
    }

    public DownloadServiceType probeURL(String urlString) throws Exception {
        URL url = new URL(urlString);
        if (!url.getProtocol().toUpperCase().equals("HTTPS") && !url.getProtocol().toUpperCase().equals("HTTP"))
            throw new Exception("url isn't http or https");

        if (urlString.toUpperCase().contains("SERVICE=WFS"))
            return DownloadServiceType.WFS;

        return DownloadServiceType.UNKNOWN;
    }

    public String findURL(Node containsOpNode) throws  Exception {
       NodeList nl= XmlDoc.xpath_nodeset(containsOpNode,"srv:SV_OperationMetadata/srv:connectPoint/gmd:CI_OnlineResource/gmd:linkage/gmd:URL");
       return nl.item(0).getTextContent();}
}
