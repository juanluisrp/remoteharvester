package net.geocat.database.linkchecker.repos;


import net.geocat.database.linkchecker.entities.LinkCheckJob;
import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public interface LinkCheckJobRepo extends CrudRepository<LinkCheckJob, String> {
}
