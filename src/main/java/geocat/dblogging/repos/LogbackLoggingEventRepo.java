package geocat.dblogging.repos;

import geocat.dblogging.entities.LogbackLoggingEvent;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface LogbackLoggingEventRepo extends CrudRepository<LogbackLoggingEvent, Long> {
    List<LogbackLoggingEvent>   findByJmsCorrelationIdOrderByTimestmp(String jms_correlation_id);
}