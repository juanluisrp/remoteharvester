package geocat.dblogging.repos;

import geocat.dblogging.entities.LogbackLoggingEvent;
import geocat.dblogging.entities.LogbackLoggingEventException;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface LogbackLoggingEventExceptionRepo extends CrudRepository<LogbackLoggingEventException, Long> {
    List<LogbackLoggingEventException> findByEventIdOrderByI(Long eventid );
}