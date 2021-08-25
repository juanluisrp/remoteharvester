package com.geocat.ingester.model.metadata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "MetadataIndicator")
@Data
public class MetadataIndicator implements Serializable {
    static final String ID_SEQ_NAME = "metadataindicator_id_seq";

    @Id
    @SequenceGenerator(name=ID_SEQ_NAME, initialValue = 100, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ID_SEQ_NAME)
    private int id;
    private String name;
    private String value;
}
