package geocat.database.entities;


import javax.persistence.*;

@Entity
@Table(name="metadata_record",
        indexes= {
                @Index(
                        name="metadata_record_endpointJobId_idx",
                        columnList="endpointJobId",
                        unique=false
                )
        })
public class MetadataRecord {
    @Id
   // @Column(columnDefinition = "varchar(40)")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long metadataRecordId;
   // @Column(columnDefinition = "varchar(40)")
    private long endpointJobId;
    private int recordNumber;
    @Column(columnDefinition = "varchar(64)")
    private String sha2;
    @Column(columnDefinition = "text")
    private String recordIdentifier;


    public String getRecordIdentifier() {
        return recordIdentifier;
    }

    public void setRecordIdentifier(String recordIdentifier) {
        this.recordIdentifier = recordIdentifier;
    }



    public long getMetadataRecordId() {
        return metadataRecordId;
    }

    public void setMetadataRecordId(long metadataRecordId) {
        this.metadataRecordId = metadataRecordId;
    }

    public long getEndpointJobId() {
        return endpointJobId;
    }

    public void setEndpointJobId(long endpointJobId) {
        this.endpointJobId = endpointJobId;
    }

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public String getSha2() {
        return sha2;
    }

    public void setSha2(String sha2) {
        this.sha2 = sha2;
    }
}
