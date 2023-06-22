package com.geocat.ingester.dao.ingester;

import com.geocat.ingester.model.ingester.IngestJob;
import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public interface IngestJobRepo extends CrudRepository<IngestJob, String> {
}
