package net.geocat.database.linkchecker.entities;

import net.geocat.database.linkchecker.entities.helper.DatasetMetadataRecord;
import net.geocat.database.linkchecker.entities.helper.ServiceMetadataRecord;

import javax.persistence.*;

@Entity
@DiscriminatorValue("RemoteDatasetMetadataRecord")
public class OperatesOnRemoteDatasetMetadataRecord extends DatasetMetadataRecord {

    @OneToOne(mappedBy = "datasetMetadataRecord",fetch=FetchType.EAGER)
    //@JoinColumn(name="operatesOnLinkId")
    private OperatesOnLink operatesOnLink;


    @Column(columnDefinition = "text" )
    private String summary;

    //---------------------------------------------------------------------------

    public OperatesOnLink getOperatesOnLink() {
        return operatesOnLink;
    }

    public void setOperatesOnLink(OperatesOnLink operatesOnLink) {
        this.operatesOnLink = operatesOnLink;
    }


    //---------------------------------------------------------------------------

    @PreUpdate
    protected void onUpdate() {
        super.onUpdate();
        summary = toString();
    }

    @PrePersist
    protected void onInsert() {
        super.onInsert();
        summary = toString();
    }

    //---------------------------------------------------------------------------

    @Override
    public String toString(){
        String result = "RemoteDatasetMetadataRecord {\n";
        result+= "      DatasetMetadataDocumentId: "+getDatasetMetadataDocumentId()+"\n";

        result += super.toString();

        result += "\n";

        result += " }";
        return result;
    }

}
