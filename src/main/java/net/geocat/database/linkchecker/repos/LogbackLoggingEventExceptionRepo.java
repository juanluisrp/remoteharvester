package net.geocat.database.linkchecker.repos;

 import net.geocat.database.linkchecker.entities.helper.LogbackLoggingEventException;
 import org.springframework.context.annotation.Scope;
 import org.springframework.data.repository.CrudRepository;
 import org.springframework.stereotype.Component;

 import java.util.List;

@Component
@Scope("prototype")
public interface LogbackLoggingEventExceptionRepo extends CrudRepository<LogbackLoggingEventException, Long> {
    List<LogbackLoggingEventException> findByEventIdOrderByI(Long eventid);
}