package geocat.model;

import geocat.csw.csw.CSWGetRecordsHandler;
import geocat.service.MetadataExploderService;
import org.w3c.dom.Document;

public class GetRecordsResponseInfo {

    CSWGetRecordsHandler cswGetRecordsHandler;

    int nrecords;  // number of records returned in GetRecords document
    Integer nextRecordNumber;  // CSW reported next record number
    int totalExpectedResults; // total number of records that the GetRecords document reports

    Document xmlParsed;


    public GetRecordsResponseInfo(String xml) throws Exception {
        this(MetadataExploderService.parseXML(xml));
    }


    public GetRecordsResponseInfo(Document xmlParsed) throws Exception {
        cswGetRecordsHandler = new CSWGetRecordsHandler(); // should probably inject with spring, but this is ok.

        this.xmlParsed = xmlParsed;

        nrecords = cswGetRecordsHandler.extractActualNumberOfRecordsReturned(xmlParsed);
        nextRecordNumber = cswGetRecordsHandler.extractNextRecordNumber(xmlParsed); // we could test to see if this is 0 if this is the lastone (but this brittle)
        totalExpectedResults = cswGetRecordsHandler.extractTotalNumberOfRecords(xmlParsed);

    }

    public int getNrecords() {
        return nrecords;
    }

    public void setNrecords(int nrecords) {
        this.nrecords = nrecords;
    }

    public Integer getNextRecordNumber() {
        return nextRecordNumber;
    }

    public void setNextRecordNumber(Integer nextRecordNumber) {
        this.nextRecordNumber = nextRecordNumber;
    }

    public int getTotalExpectedResults() {
        return totalExpectedResults;
    }

    public void setTotalExpectedResults(int totalExpectedResults) {
        this.totalExpectedResults = totalExpectedResults;
    }

    public Document getXmlParsed() {
        return xmlParsed;
    }

    public void setXmlParsed(Document xmlParsed) {
        this.xmlParsed = xmlParsed;
    }
}
