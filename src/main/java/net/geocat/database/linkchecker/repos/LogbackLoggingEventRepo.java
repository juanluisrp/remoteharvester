package net.geocat.database.linkchecker.repos;

 import net.geocat.database.linkchecker.entities.helper.LogbackLoggingEvent;
 import org.springframework.context.annotation.Scope;
 import org.springframework.data.repository.CrudRepository;
 import org.springframework.stereotype.Component;

 import java.util.List;

@Component
@Scope("prototype")
public interface LogbackLoggingEventRepo extends CrudRepository<LogbackLoggingEvent, Long> {
    List<LogbackLoggingEvent> findByJmsCorrelationIdOrderByTimestmp(String jms_correlation_id);
}