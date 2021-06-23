package com.geocat.ingester.model.metadata;

import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "OperationAllowed")
@Data
public class OperationAllowed {
    @EmbeddedId
    private OperationAllowedId id = new OperationAllowedId();
}
