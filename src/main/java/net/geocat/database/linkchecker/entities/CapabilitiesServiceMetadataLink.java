package net.geocat.database.linkchecker.entities;

import net.geocat.database.linkchecker.entities.helper.PartialDownloadHint;
import net.geocat.database.linkchecker.entities.helper.RetrievableSimpleLink;

import javax.persistence.*;

@Entity
public class CapabilitiesServiceMetadataLink extends RetrievableSimpleLink {


    public CapabilitiesServiceMetadataLink() {
        super.setPartialDownloadHint(PartialDownloadHint.CAPABILITIES_ONLY);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long capabilitiesServiceMetadataLinkId;


    @Column(columnDefinition = "text")
    private String summary;



    //---------------------------------------------------------------------------



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
        String result = "CapabilitiesServiceMetadataLink {\n";
        result += "      capabilitiesServiceMetadataLinkId: "+capabilitiesServiceMetadataLinkId+"\n";
        result += "\n";
        result += super.toString();

        result += "  }";
        return result;
    }
}
