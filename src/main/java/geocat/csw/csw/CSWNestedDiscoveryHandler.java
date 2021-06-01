package geocat.csw.csw;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
public class CSWNestedDiscoveryHandler {

    Logger logger = LoggerFactory.getLogger(CSWNestedDiscoveryHandler.class);

    public List<List<String>> extractNestedDiscoveryEndpoints(String getRecordsResponseXML) throws Exception {

        List<List<String>> result = new ArrayList<>();


        Document doc = XMLTools.parseXML(getRecordsResponseXML);

        //one for each Discovery metadata record
        NodeList nodeList = XMLTools.xpath_nodeset(doc, "//GetRecordsResponse/SearchResults/MD_Metadata/identificationInfo/SV_ServiceIdentification/containsOperations[SV_OperationMetadata/operationName/CharacterString = 'GetCapabilities']");
        logger.debug("     * found " + nodeList.getLength() + " discovery services");
        for (int t = 0; t < nodeList.getLength(); t++) {
            Node discoveryGetCapInfo = nodeList.item(t);
            NodeList urls = XMLTools.xpath_nodeset(discoveryGetCapInfo, "SV_OperationMetadata/connectPoint/CI_OnlineResource/linkage/URL");
            List<String> potentialUrls = new ArrayList<>();
            //one for each of the URLs in ONE Discovery metadata record
            for (int u = 0; u < urls.getLength(); u++) {
                String discoveryGetCapUrl = urls.item(u).getTextContent();
                if (discoveryGetCapUrl.isEmpty())
                    continue;
                URL uu = new URL(discoveryGetCapUrl);
                if (!uu.getProtocol().equalsIgnoreCase("HTTP") && !uu.getProtocol().equalsIgnoreCase("HTTPS"))
                    throw new Exception("discovery URL isn't HTTP or HTTPS - security violation");
                potentialUrls.add(uu.toString());
            }
            if (!potentialUrls.isEmpty())
                result.add(potentialUrls);
            //if any of these urls are already in the DB, we do nothing
            //boolean noActionRequired = endpointJobService.areTheseUrlsInDB(endPointDetectedEvent.getHarvesterId(),potentialUrls);
            //so, we can add this one.  We always use the first one...
//            if (!noActionRequired) {
//                String discovreyURL = potentialUrls.get(0);
//                URL u = new URL(discovreyURL);
//                if (!u.getProtocol().equalsIgnoreCase("HTTP") && !u.getProtocol().equalsIgnoreCase("HTTPS"))
//                    throw new Exception("discovery URL isn't HTTP or HTTPS - security violation");
//                result.add(u.toString());
//            }
        }
        return result;
    }
}
