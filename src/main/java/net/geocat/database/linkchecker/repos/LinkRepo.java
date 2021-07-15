package net.geocat.database.linkchecker.repos;

import net.geocat.database.linkchecker.entities.Link;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LinkRepo extends CrudRepository<Link, Long> {

    List<Link>   findByLinkCheckJobId (String linkCheckJobId);

    long countByLinkCheckJobId(String LinkCheckJobId);

    @Query(value = "Select count(*) from links   where linkcheckjobid = ?1 and (linkState = 'COMPLETE' or linkState='ERROR') ",
            nativeQuery = true
    )
    long countCompletedState(String LinkCheckJobId);
}