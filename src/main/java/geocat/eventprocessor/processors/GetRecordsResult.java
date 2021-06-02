package geocat.eventprocessor.processors;

import org.w3c.dom.Document;

public class GetRecordsResult {

    private String xmlGetRecordsResult;
    private int numberRecordsReturned;
    private Document parsedXML;

    public GetRecordsResult(String xmlGetRecordsResult, int numberRecordsReturned,Document parsedXML) {
        this.xmlGetRecordsResult = xmlGetRecordsResult;
        this.numberRecordsReturned = numberRecordsReturned;
        this.parsedXML = parsedXML;
    }

    public String getXmlGetRecordsResult() {
        return xmlGetRecordsResult;
    }

    public void setXmlGetRecordsResult(String xmlGetRecordsResult) {
        this.xmlGetRecordsResult = xmlGetRecordsResult;
    }

    public int getNumberRecordsReturned() {
        return numberRecordsReturned;
    }

    public void setNumberRecordsReturned(int numberRecordsReturned) {
        this.numberRecordsReturned = numberRecordsReturned;
    }

    public Document getParsedXML() {
        return parsedXML;
    }

    public void setParsedXML(Document parsedXML) {
        this.parsedXML = parsedXML;
    }
}
