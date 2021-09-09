package com.geocat.ingester.dao.ingester;

import com.geocat.ingester.model.ingester.LogbackLoggingEventException;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface LogbackLoggingEventExceptionRepo extends CrudRepository<LogbackLoggingEventException, Long> {
    List<LogbackLoggingEventException> findByEventIdOrderByI(Long eventid);
}
