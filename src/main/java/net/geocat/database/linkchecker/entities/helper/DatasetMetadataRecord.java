package net.geocat.database.linkchecker.entities.helper;


import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="dataset_record_type",
        discriminatorType = DiscriminatorType.STRING)
public class DatasetMetadataRecord extends MetadataRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long datasetMetadataDocumentId;

    private String datasetIdentifier;


    //---------------------------------------------------------------------------

    public long getDatasetMetadataDocumentId() {
        return datasetMetadataDocumentId;
    }

    public void setDatasetMetadataDocumentId(long datasetMetadataDocumentId) {
        this.datasetMetadataDocumentId = datasetMetadataDocumentId;
    }

    public String getDatasetIdentifier() {
        return datasetIdentifier;
    }

    public void setDatasetIdentifier(String datasetIdentifier) {
        this.datasetIdentifier = datasetIdentifier;
    }


    //---------------------------------------------------------------------------


    protected void onUpdate() {
    }


    protected void onInsert() {
    }

    //---------------------------------------------------------------------------
    @Override
    public String toString(){
        String result = super.toString();

        result+= "     dataset Identifier: "+datasetIdentifier+"\n";

        return result;
    }
}
