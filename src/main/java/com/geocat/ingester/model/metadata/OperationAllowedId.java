package com.geocat.ingester.model.metadata;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class OperationAllowedId implements Serializable {
    private static final long serialVersionUID = -5759713154514715316L;

    private int metadataId;
    private int groupId;
    private int operationId;

    public OperationAllowedId() {
        super();
    }
}
