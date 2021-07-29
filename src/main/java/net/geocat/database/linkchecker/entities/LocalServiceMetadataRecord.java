package net.geocat.database.linkchecker.entities;

import net.geocat.database.linkchecker.entities.helper.ServiceMetadataDocumentState;
import net.geocat.database.linkchecker.entities.helper.ServiceMetadataRecord;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("LocalServiceMetadataRecord")
public class LocalServiceMetadataRecord extends ServiceMetadataRecord {

    @Column(columnDefinition = "varchar(40)")
    private String linkCheckJobId;

    private long harvesterMetadataRecordId;



    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20)")
    ServiceMetadataDocumentState state;

    @Column(columnDefinition = "text" )
    private String summary;



    public String getLinkCheckJobId() {
        return linkCheckJobId;
    }

    public void setLinkCheckJobId(String linkCheckJobId) {
        this.linkCheckJobId = linkCheckJobId;
    }



    public long getHarvesterMetadataRecordId() {
        return harvesterMetadataRecordId;
    }

    public void setHarvesterMetadataRecordId(long harvesterMetadataRecordId) {
        this.harvesterMetadataRecordId = harvesterMetadataRecordId;
    }


    public ServiceMetadataDocumentState getState() {
        return state;
    }

    public void setState(ServiceMetadataDocumentState state) {
        this.state = state;
    }



    //---------------------------------------------------------------------------

    @PreUpdate
    protected void onUpdate() {
        summary = toString();
        super.onUpdate();
    }

    @PrePersist
    protected void onInsert() {
        summary = toString();
        super.onInsert();
    }

    //---------------------------------------------------------------------------

    @Override
    public String toString(){
        String result = "ServiceMetadataDocument {\n";
        result+= "     serviceMetadataDocumentId: "+getServiceMetadataDocumentId()+"\n";
        result+= "     linkCheckJobId: "+linkCheckJobId+"\n";
        result+= "     harvesterMetadataRecordId: "+harvesterMetadataRecordId+"\n";
        result+= "     state: "+state+"\n";


        result += super.toString();

        result += "\n";

        result += " }";
        return result;
    }
}
