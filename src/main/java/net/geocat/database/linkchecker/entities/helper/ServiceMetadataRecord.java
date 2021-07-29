package net.geocat.database.linkchecker.entities.helper;

import net.geocat.database.linkchecker.entities.OperatesOnLink;
import net.geocat.database.linkchecker.entities.ServiceDocumentLink;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="service_record_type",
        discriminatorType = DiscriminatorType.STRING)
public class ServiceMetadataRecord extends MetadataRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long serviceMetadataDocumentId;

    @Column(columnDefinition = "text" )
    //i.e. view/download/discovery
    private String metadataServiceType;

    private Integer numberOfLinksFound;
    private Integer numberOfOperatesOnFound;

    @OneToMany(mappedBy= "serviceMetadataRecord",
            cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
   // @JoinColumn(name="serviceMetadataRecordId")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<ServiceDocumentLink> serviceDocumentLinks;

    @OneToMany(mappedBy= "serviceMetadataRecord",
            cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
   // @JoinColumn(name="serviceMetadataRecordId")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<OperatesOnLink> operatesOnLinks;


    //---------------------------------------------------------------------------


    public long getServiceMetadataDocumentId() {
        return serviceMetadataDocumentId;
    }

    public void setServiceMetadataDocumentId(long serviceMetadataDocumentId) {
        this.serviceMetadataDocumentId = serviceMetadataDocumentId;
    }

    public String getMetadataServiceType() {
        return metadataServiceType;
    }

    public void setMetadataServiceType(String metadataServiceType) {
        this.metadataServiceType = metadataServiceType;
    }

    public Integer getNumberOfLinksFound() {
        return numberOfLinksFound;
    }

    public void setNumberOfLinksFound(Integer numberOfLinksFound) {
        this.numberOfLinksFound = numberOfLinksFound;
    }

    public Integer getNumberOfOperatesOnFound() {
        return numberOfOperatesOnFound;
    }

    public void setNumberOfOperatesOnFound(Integer numberOfOperatesOnFound) {
        this.numberOfOperatesOnFound = numberOfOperatesOnFound;
    }

    public List<ServiceDocumentLink> getServiceDocumentLinks() {
        return serviceDocumentLinks;
    }

    public void setServiceDocumentLinks(List<ServiceDocumentLink> serviceDocumentLinks) {
        this.serviceDocumentLinks = serviceDocumentLinks;
    }

    public List<OperatesOnLink> getOperatesOnLinks() {
        return operatesOnLinks;
    }

    public void setOperatesOnLinks(List<OperatesOnLink> operatesOnLinks) {
        this.operatesOnLinks = operatesOnLinks;
    }


    //---------------------------------------------------------------------------




    protected void onUpdate() {
        update();
    }


    protected void onInsert() {
       update();
    }

    protected void update() {
        if (serviceDocumentLinks != null)
            numberOfLinksFound = serviceDocumentLinks.size();
        if (operatesOnLinks != null)
            numberOfOperatesOnFound = operatesOnLinks.size();
    }

    //---------------------------------------------------------------------------

    @Override
    public String toString(){
        update();
        String result = super.toString();

        result+= "     metadataServiceType: "+metadataServiceType+"\n";
        if (numberOfLinksFound != null)
            result+= "     numberOfLinksFound: "+numberOfLinksFound+"\n";
        if (numberOfOperatesOnFound != null)
            result+= "     numberOfOperatesOnFound: "+numberOfOperatesOnFound+"\n";

        return result;
    }
}
