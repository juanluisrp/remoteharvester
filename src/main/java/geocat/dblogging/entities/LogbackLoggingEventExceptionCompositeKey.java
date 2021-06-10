package geocat.dblogging.entities;

import javax.persistence.Column;
import java.io.Serializable;

public class LogbackLoggingEventExceptionCompositeKey implements Serializable {
    @Column(columnDefinition = "bigint")
    private long eventId;

    @Column(columnDefinition = "smallint")
    private short i;

}
