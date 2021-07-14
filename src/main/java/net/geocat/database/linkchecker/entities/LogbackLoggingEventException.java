package net.geocat.database.linkchecker.entities;


import javax.persistence.*;
//taken from;
// https://raw.githubusercontent.com/qos-ch/logback/master/logback-classic/src/main/resources/ch/qos/logback/classic/db/script/postgresql.sql

@Entity
@Table(name = "logging_event_exception"
//        ,
//        indexes= {
//                @Index(
//                        name="harvestJobId_idx",
//                        columnList="harvestJobId",
//                        unique=false
//                )
//        }
)
@IdClass( LogbackLoggingEventExceptionCompositeKey.class)
public class LogbackLoggingEventException {

    @Column(columnDefinition = "bigint")
    @Id
    private long eventId;

    @Column(columnDefinition = "smallint")
    @Id
    private short i;

    @Column(columnDefinition = "varchar(254)")
    private String traceLine;

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public short getI() {
        return i;
    }

    public void setI(short i) {
        this.i = i;
    }

    public String getTraceLine() {
        return traceLine;
    }

    public void setTraceLine(String traceLine) {
        this.traceLine = traceLine;
    }
}
