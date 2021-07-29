package net.geocat.database.linkchecker.entities;

import net.geocat.database.linkchecker.entities.helper.DatasetMetadataRecord;
import net.geocat.database.linkchecker.entities.helper.PartialDownloadHint;
import net.geocat.database.linkchecker.entities.helper.RetrievableSimpleLink;
import net.geocat.database.linkchecker.entities.helper.ServiceMetadataRecord;

import javax.persistence.*;

@Entity
public class OperatesOnLink extends RetrievableSimpleLink {

    public OperatesOnLink() {
        this.setPartialDownloadHint(PartialDownloadHint.METADATA_ONLY);
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long operatesOnLinkId;

    @Column(columnDefinition = "text")
    String uuidref;



    @Column(columnDefinition = "text" )
    String summary;

    @ManyToOne(fetch=FetchType.EAGER)
    //@JoinColumn(name="serviceMetadataId")
    private ServiceMetadataRecord serviceMetadataRecord;


    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "datasetMetadataRecordId" )
    private OperatesOnRemoteDatasetMetadataRecord datasetMetadataRecord;


    //---------------------------------------------------------------------------


    public ServiceMetadataRecord getServiceMetadataRecord() {
        return serviceMetadataRecord;
    }

    public void setServiceMetadataRecord(ServiceMetadataRecord serviceMetadataRecord) {
        this.serviceMetadataRecord = serviceMetadataRecord;
    }

    public long getOperatesOnLinkId() {
        return operatesOnLinkId;
    }

    public void setOperatesOnLinkId(long operatesOnLinkId) {
        this.operatesOnLinkId = operatesOnLinkId;
    }

    public String getUuidref() {
        return uuidref;
    }

    public void setUuidref(String uuidref) {
        this.uuidref = uuidref;
    }



    public DatasetMetadataRecord getDatasetMetadataRecord() {
        return datasetMetadataRecord;
    }

    public void setDatasetMetadataRecord(OperatesOnRemoteDatasetMetadataRecord datasetMetadataRecord) {
        this.datasetMetadataRecord = datasetMetadataRecord;
    }

//---------------------------------------------------------------------------

    @PreUpdate
    private void onUpdate() {
        this.summary = this.toString();
    }

    @PrePersist
    private void onInsert() {
        this.summary = this.toString();
    }

    //---------------------------------------------------------------------------


    @Override
    public String toString() {
        String result = "OperatesOnLink {\n";
        result += "      operatesOnLinkId: "+operatesOnLinkId+"\n";
        if ( (uuidref != null) && (!uuidref.isEmpty()) )
            result += "      uuidref: "+uuidref+"\n";


        result += super.toString();
        result += "      has dataset Metadata Record :"+(datasetMetadataRecord != null)+"\n";
        if (datasetMetadataRecord !=null) {
            result += "      dataset Metadata Record file identifier: "+datasetMetadataRecord.getFileIdentifier()+"\n";
            result += "      dataset Metadata Record dataset identifier: "+datasetMetadataRecord.getDatasetIdentifier()+"\n";
        }
//        if ( (serviceMetadataRecord != null)   )
//            result += "      serviceMetadataRecord record identifier: "+ serviceMetadataRecord.getFileIdentifier()+"\n";
//        if ( (localServiceMetadataRecord != null)   )
//            result += "      serviceMetadataRecord Id: "+ sServiceMetadataRecord.getServiceMetadataDocumentId()+"\n";

        result += "  }";
        return result;
    }

}
