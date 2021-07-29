package net.geocat.database.linkchecker.entities.helper;

import javax.persistence.*;

//taken from;
// https://raw.githubusercontent.com/qos-ch/logback/master/logback-classic/src/main/resources/ch/qos/logback/classic/db/script/postgresql.sql


@Entity
@Table(name = "logging_event"
        ,
        indexes = {
                @Index(
                        name = "logging_correlationid_idx",
                        columnList = "jms_correlation_id",
                        unique = false
                )
        }
)
public class LogbackLoggingEvent {

    @Column(columnDefinition = "bigint")
    public long timestmp;

    @Column(name="formatted_message",columnDefinition = "text")
    public String formattedMessage;

    @Column(name="logger_name",columnDefinition = "varchar(254)")
    public String loggerName;

    @Column(name="level_string",columnDefinition = "varchar(254)")
    public String levelString;

    @Column(name="thread_name",columnDefinition = "varchar(254)")
    public String threadName;

    @Column(name="reference_flag",columnDefinition = "smallint")
    public short referenceFlag;

    @Column(columnDefinition = "varchar(254)")
    public String arg0;
    @Column(columnDefinition = "varchar(254)")
    public String arg1;
    @Column(columnDefinition = "varchar(254)")
    public String arg2;
    @Column(columnDefinition = "varchar(254)")
    public String arg3;

    @Column(name="caller_filename",columnDefinition = "varchar(254)")
    public String callerFilename;

    @Column(name="caller_class",columnDefinition = "varchar(254)")
    public String callerClass;

    @Column(name="caller_method",columnDefinition = "varchar(254)")
    public String callerMethod;

    @Column(name="caller_line",columnDefinition = "char(4)")
    public String callerLine;

    @Column(name="jms_correlation_id",columnDefinition = "varchar(254)")
    public String jmsCorrelationId;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="event_id",columnDefinition = "BIGINT")
    public long eventId;


}
