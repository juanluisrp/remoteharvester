package com.geocat.ingester.model.ingester;


import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
//taken from;
// https://raw.githubusercontent.com/qos-ch/logback/master/logback-classic/src/main/resources/ch/qos/logback/classic/db/script/postgresql.sql

//This is no longer required!
//@Entity
@Table(name = "logging_event_property"
//        ,
//        indexes= {
//                @Index(
//                        name="harvestJobId_idx",
//                        columnList="harvestJobId",
//                        unique=false
//                )
//        }
)
@IdClass(LogbackLoggingEventPropertyCompositeKey.class)
public class LogbackLoggingEventProperty {

    @Column(columnDefinition = "bigint")
    @Id
    private long eventId;

    @Column(columnDefinition = "varchar(254)")
    @Id
    private String mappedKey;

    @Column(columnDefinition = "varchar(1024)")
    private String mappedValue;
}
