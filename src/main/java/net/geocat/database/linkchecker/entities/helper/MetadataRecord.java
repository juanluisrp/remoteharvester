package net.geocat.database.linkchecker.entities.helper;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class MetadataRecord {

    @Column(columnDefinition = "varchar(64)")
    private String sha2;

    @Column(columnDefinition = "text")
    private String fileIdentifier;

    @Column(columnDefinition = "text" )
    //i.e. will be service
    private String metadataRecordType;

    //---------------------------------------------------------------------------

    public String getSha2() {
        return sha2;
    }

    public void setSha2(String sha2) {
        this.sha2 = sha2;
    }

    public String getFileIdentifier() {
        return fileIdentifier;
    }

    public void setFileIdentifier(String recordIdentifier) {
        this.fileIdentifier = recordIdentifier;
    }

    public String getMetadataRecordType() {
        return metadataRecordType;
    }

    public void setMetadataRecordType(String metadataRecordType) {
        this.metadataRecordType = metadataRecordType;
    }


    //---------------------------------------------------------------------------

    @Override
    public String toString(){
        String result = "";
        result+= "     sha2: "+sha2+"\n";
        result+= "     fileIdentifier: "+ fileIdentifier +"\n";
        result+= "     metadataRecordType: "+metadataRecordType+"\n";
        return result;
    }
}
