package net.geocat.database.linkchecker.repos;

 import net.geocat.database.linkchecker.entities.LogbackLoggingEvent;
 import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface LogbackLoggingEventRepo extends CrudRepository<LogbackLoggingEvent, Long> {
    List<LogbackLoggingEvent> findByJmsCorrelationIdOrderByTimestmp(String jms_correlation_id);
}