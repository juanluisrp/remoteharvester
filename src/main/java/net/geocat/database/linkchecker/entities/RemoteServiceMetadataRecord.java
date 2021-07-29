package net.geocat.database.linkchecker.entities;

import net.geocat.database.linkchecker.entities.helper.ServiceMetadataRecord;

import javax.persistence.*;

@Entity
@DiscriminatorValue("RemoteServiceMetadataRecord")
public class RemoteServiceMetadataRecord extends ServiceMetadataRecord {

//    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
//    @JoinColumn(name = "remoteServiceMetadataRecordLinkId" )
    @OneToOne(mappedBy = "remoteServiceMetadataRecord")
    private RemoteServiceMetadataRecordLink remoteServiceMetadataRecordLink;

    @Column(columnDefinition = "text" )
    private String summary;

    //---------------------------------------------------------------------------

    public RemoteServiceMetadataRecordLink getRemoteServiceMetadataRecordLink() {
        return remoteServiceMetadataRecordLink;
    }

    public void setRemoteServiceMetadataRecordLink(RemoteServiceMetadataRecordLink remoteServiceMetadataRecordLink) {
        this.remoteServiceMetadataRecordLink = remoteServiceMetadataRecordLink;
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
        String result = "RemoteServiceMetadataRecord {\n";
        result+= "     serviceMetadataDocumentId: "+getServiceMetadataDocumentId()+"\n";

        result += super.toString();

        result += "\n";

        result += " }";
        return result;
    }
}
