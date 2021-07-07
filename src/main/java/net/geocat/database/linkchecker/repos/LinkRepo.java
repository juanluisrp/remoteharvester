package net.geocat.database.linkchecker.repos;

import net.geocat.database.linkchecker.entities.Link;
import org.springframework.data.repository.CrudRepository;

public interface LinkRepo extends CrudRepository<Link, Long> {
}