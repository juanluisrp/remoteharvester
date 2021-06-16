package geocat.model;


import geocat.csw.csw.XMLTools;
import org.w3c.dom.Document;

import java.net.URL;

public class HarvesterConfig {

    public static int DEFAULT_NRECORDS = 20;
    private String longTermTag;  //i.e. country name
    private boolean lookForNestedDiscoveryService; //true for poland
    private String filter;  //CSW <ogc:Filter>
    private String url; // endpoint for GetCapabilities
    private String processID; // GUID for the harvest (used as JMS Correlation ID).  Provided by server
    private int numberOfRecordsPerRequest; // defaults to 20 records in a GetRecords request
    private String getRecordQueueHint; // which queue set to use.  blank=auto determined.  Otherwise "PARALLEL#" #=2,3,4
    private ProblematicResultsConfiguration problematicResultsConfiguration;

    public String getGetRecordQueueHint() {
        return getRecordQueueHint;
    }

    public void setGetRecordQueueHint(String getRecordQueueHint) {
        this.getRecordQueueHint = getRecordQueueHint;
    }

    public ProblematicResultsConfiguration getProblematicResultsConfiguration() {
        return problematicResultsConfiguration;
    }

    public void setProblematicResultsConfiguration(ProblematicResultsConfiguration problematicResultsConfiguration) {
        this.problematicResultsConfiguration = problematicResultsConfiguration;
    }

    public boolean isLookForNestedDiscoveryService() {
        return lookForNestedDiscoveryService;
    }

    public void setLookForNestedDiscoveryService(boolean lookForNestedDiscoveryService) {
        this.lookForNestedDiscoveryService = lookForNestedDiscoveryService;
    }

    public String getLongTermTag() {
        return longTermTag;
    }

    public void setLongTermTag(String longTermTag) {
        this.longTermTag = longTermTag;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProcessID() {
        return processID;
    }

    public void setProcessID(String processID) {
        this.processID = processID;
    }

    public void validate() throws Exception {
        if ((url == null) || (url.isEmpty()))
            throw new Exception("No URL given for remote CSW");
        URL _url = new URL(url);
        if (!_url.getProtocol().equalsIgnoreCase("HTTP") && !_url.getProtocol().equalsIgnoreCase("HTTPS"))
            throw new Exception("URL isn't http or https");

        if ((filter != null) && (!filter.isEmpty())) { //filter present
            Document filterDoc = XMLTools.parseXML(filter);
            String rootNodeName = filterDoc.getFirstChild().getNodeName();
            if (rootNodeName != "ogc:Filter")
                throw new Exception("filter doesn't start with <ogc:Filter>");
        }
        if (problematicResultsConfiguration == null)
            problematicResultsConfiguration = new ProblematicResultsConfiguration();
        problematicResultsConfiguration.validate();

        if (numberOfRecordsPerRequest <= 0)
            numberOfRecordsPerRequest = DEFAULT_NRECORDS;

        if (numberOfRecordsPerRequest > 500) // unreasonable
            numberOfRecordsPerRequest = 500;
    }

    @Override
    public String toString() {
        return "{processID=" + processID + ", urls=" + url + "}";
    }

    public int getNumberOfRecordsPerRequest() {
        return numberOfRecordsPerRequest;
    }

    public void setNumberOfRecordsPerRequest(int numberOfRecordsPerRequest) {
        this.numberOfRecordsPerRequest = numberOfRecordsPerRequest;
    }
}
