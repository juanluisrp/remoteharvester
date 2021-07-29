package net.geocat.database.linkchecker.entities;

import net.geocat.database.linkchecker.entities.helper.DatasetMetadataRecord;

import javax.persistence.*;

@Entity
@DiscriminatorValue("CapabilitiesRemoteDSMDDocument")
public class CapabilitiesRemoteDatasetMetadataDocument extends DatasetMetadataRecord  {

    @OneToOne(mappedBy = "capabilitiesRemoteDatasetMetadataDocument",fetch=FetchType.EAGER)
   // @JoinColumn(name="capabilitiesDatasetMetadataLinkId")
    private CapabilitiesDatasetMetadataLink capabilitiesRemoteDatasetMetadataDocumentLink;

    @Column(columnDefinition = "text" )
    private String summary;

    //---------------------------------------------------------------------------

    public CapabilitiesDatasetMetadataLink getCapabilitiesDatasetMetadataLink() {
        return capabilitiesRemoteDatasetMetadataDocumentLink;
    }

    public void setCapabilitiesDatasetMetadataLink(CapabilitiesDatasetMetadataLink capabilitiesDatasetMetadataLink) {
        this.capabilitiesRemoteDatasetMetadataDocumentLink = capabilitiesDatasetMetadataLink;
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
        String result = "CapabilitiesRemoteDatasetMetadataDocument {\n";
        result+= "      DatasetMetadataDocumentId: "+getDatasetMetadataDocumentId()+"\n";

        result += super.toString();

        result += "\n";

        result += " }";
        return result;
    }
}
