package net.geocat.database.linkchecker.repos;

import net.geocat.database.linkchecker.entities.OperatesOnLink;
import net.geocat.database.linkchecker.entities.ServiceDocumentLink;
import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public interface OperatesOnLinkRepo extends CrudRepository<OperatesOnLink, Long> {

}