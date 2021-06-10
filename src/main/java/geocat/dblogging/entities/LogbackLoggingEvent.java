package geocat.dblogging.entities;

import javax.persistence.*;

//taken from;
// https://raw.githubusercontent.com/qos-ch/logback/master/logback-classic/src/main/resources/ch/qos/logback/classic/db/script/postgresql.sql


@Entity
@Table(name="logging_event"
        ,
        indexes= {
                @Index(
                        name="logging_correlationid_idx",
                        columnList="jmsCorrelationId",
                        unique=false
                )
        }
        )
public class LogbackLoggingEvent {

    @Column(columnDefinition = "bigint")
    private long timestmp;

    @Column(columnDefinition = "text")
    private String formattedMessage;

    @Column(columnDefinition = "varchar(254)")
    private String loggerName;

    @Column(columnDefinition = "varchar(254)")
    private String levelString;

    @Column(columnDefinition = "varchar(254)")
    private String threadName;

    @Column(columnDefinition = "smallint")
    private short referenceFlag;

    @Column(columnDefinition = "varchar(254)")
    private String arg0;
    @Column(columnDefinition = "varchar(254)")
    private String arg1;
    @Column(columnDefinition = "varchar(254)")
    private String arg2;
    @Column(columnDefinition = "varchar(254)")
    private String arg3;

    @Column(columnDefinition = "varchar(254)")
    private String callerFilename;

    @Column(columnDefinition = "varchar(254)")
    private String callerClass;

    @Column(columnDefinition = "varchar(254)")
    private String callerMethod;

    @Column(columnDefinition = "char(4)")
    private String callerLine;

    @Column(columnDefinition = "varchar(254)")
    private String jmsCorrelationId;


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private long   eventId;


}
