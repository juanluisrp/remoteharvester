package geocat.database.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;


//create table record_set (record_set_id varchar(40), endpoint_job_id varchar(40), harvest_job_id varchar(40), start_record_number int, end_record_number int, expected_number_records int, actual_number_records int, get_record_response text, last_set bool);
@Entity
@Table(name="record_set",
        indexes= {
                @Index(
                        name="endpointJobId_idx",
                        columnList="endpointJobId",
                        unique=false
                )
        })
public class RecordSet {
    @Id
    private String recordSetId;
    private String endpointJobId;
    private String harvestJobId;
    private int startRecordNumber;
    private int endRecordNumber;
    private int expectedNumberRecords;
    private int actualNumberRecords;
    private String getRecordResponse;
    private boolean lastSet; // this is last one


    public String getRecordSetId() {
        return recordSetId;
    }

    public void setRecordSetId(String recordSetId) {
        this.recordSetId = recordSetId;
    }

    public String getEndpointJobId() {
        return endpointJobId;
    }

    public void setEndpointJobId(String endpointJobId) {
        this.endpointJobId = endpointJobId;
    }

    public String getHarvestJobId() {
        return harvestJobId;
    }

    public void setHarvestJobId(String harvestJobId) {
        this.harvestJobId = harvestJobId;
    }

    public int getStartRecordNumber() {
        return startRecordNumber;
    }

    public void setStartRecordNumber(int startRecordNumber) {
        this.startRecordNumber = startRecordNumber;
    }

    public int getEndRecordNumber() {
        return endRecordNumber;
    }

    public void setEndRecordNumber(int endRecordNumber) {
        this.endRecordNumber = endRecordNumber;
    }

    public int getExpectedNumberRecords() {
        return expectedNumberRecords;
    }

    public void setExpectedNumberRecords(int expectedNumberRecords) {
        this.expectedNumberRecords = expectedNumberRecords;
    }

    public int getActualNumberRecords() {
        return actualNumberRecords;
    }

    public void setActualNumberRecords(int actualNumberRecords) {
        this.actualNumberRecords = actualNumberRecords;
    }

    public String getGetRecordResponse() {
        return getRecordResponse;
    }

    public void setGetRecordResponse(String getRecordResponse) {
        this.getRecordResponse = getRecordResponse;
    }

    public boolean isLastSet() {
        return lastSet;
    }

    public void setLastSet(boolean lastSet) {
        this.lastSet = lastSet;
    }
}
