package net.geocat.database.linkchecker.repos2;

import net.geocat.database.linkchecker.entities2.Link;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
@Component
@Scope("prototype")
public interface LinkRepo extends Map<Link, Long> {

    List<Link>   findByLinkCheckJobId (String linkCheckJobId);

    long countByLinkCheckJobId(String LinkCheckJobId);

    @Query(value = "Select count(*) from links   where linkcheckjobid = ?1 and (linkState = 'COMPLETE' or linkState='ERROR') ",
            nativeQuery = true
    )
    long countCompletedState(String LinkCheckJobId);
}