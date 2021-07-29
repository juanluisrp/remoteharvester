package net.geocat.database.linkchecker.repos2;


import net.geocat.database.linkchecker.entities2.LinkCheckJob;
import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Scope("prototype")
public interface LinkCheckJobRepo extends Map<LinkCheckJob, String> {
}
