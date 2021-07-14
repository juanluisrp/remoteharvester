package net.geocat.database.linkchecker.entities;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Objects;

public class LogbackLoggingEventExceptionCompositeKey implements Serializable {
    @Column(columnDefinition = "bigint")
    private long eventId;

    @Column(columnDefinition = "smallint")
    private short i;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogbackLoggingEventExceptionCompositeKey)) return false;
        LogbackLoggingEventExceptionCompositeKey that = (LogbackLoggingEventExceptionCompositeKey) o;
        return eventId == that.eventId && i == that.i;
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, i);
    }
}
