package com.geocat.ingester.model.ingester;

import javax.persistence.Column;
import java.io.Serializable;

public class LogbackLoggingEventPropertyCompositeKey implements Serializable {
    @Column(columnDefinition = "bigint")
    private long eventId;

    @Column(columnDefinition = "varchar(254)")
    private String mappedKey;
}
