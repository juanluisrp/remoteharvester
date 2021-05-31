package geocat.csw.csw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CSWGetRecordsHandler {

    Logger logger = LoggerFactory.getLogger(CSWGetRecordsHandler.class);
    Pattern SearchResultsTagPattern = Pattern.compile("<[^:<>]+:SearchResults [^>]+numberOfRecordsReturned=\"(\\d+)\"[^>]+>");


    public int extractTotalNumberOfRecords(String getRecordsResponseXML) throws Exception {
        Document doc = XMLTools.parseXML(getRecordsResponseXML);
        String nrecordsString = XMLTools.xpath_attribute(doc,"/GetRecordsResponse/SearchResults","numberOfRecordsMatched");
        return Integer.parseInt(nrecordsString);
    }

    public int extractNextRecordNumber(String getRecordsResponseXML) throws Exception {
        Document doc = XMLTools.parseXML(getRecordsResponseXML);
        String nextRecordString = XMLTools.xpath_attribute(doc,"/GetRecordsResponse/SearchResults","nextRecord");
        return Integer.parseInt(nextRecordString);
    }

    //this is MUCH faster than parsing the xml
    // parse XML = 14seconds
    //   this = <0.1 second
    // NOTE: if the response is an error, this can be very slow.
    //<csw:SearchResults numberOfRecordsMatched="138" numberOfRecordsReturned="10" elementSet="full" nextRecord="41">
    public int extractActualNumberOfRecordsReturned(String getRecordsResponseXML) throws Exception {
        if (!getRecordsResponseXML.contains("SearchResults"))
            throw new Exception("XML doesn't contain SearchResults");
        Matcher matcher = SearchResultsTagPattern.matcher(getRecordsResponseXML);
        if (!matcher.find())
            throw new Exception("couldn't parse xml");
        String nreturned_string = matcher.group(1); //i.e. numberOfRecordsMatched="138" numberOfRecordsReturned="10" elementSet="full" nextRecord="41"
        return Integer.parseInt(nreturned_string);
    }
}
