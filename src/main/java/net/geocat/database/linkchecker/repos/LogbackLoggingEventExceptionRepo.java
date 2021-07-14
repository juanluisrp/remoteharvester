package net.geocat.database.linkchecker.repos;

 import net.geocat.database.linkchecker.entities.LogbackLoggingEventException;
 import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface LogbackLoggingEventExceptionRepo extends CrudRepository<LogbackLoggingEventException, Long> {
    List<LogbackLoggingEventException> findByEventIdOrderByI(Long eventid);
}