package geocat.eventprocessor.processors;

public class GetRecordsResult {

    private String xmlGetRecordsResult;
    private int numberRecordsReturned;

    public GetRecordsResult(String xmlGetRecordsResult, int numberRecordsReturned) {
        this.xmlGetRecordsResult = xmlGetRecordsResult;
        this.numberRecordsReturned = numberRecordsReturned;
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
}
