package geocat.database.repos;

import geocat.database.entities.RecordSet;
import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Scope("prototype")
public interface RecordSetRepo extends CrudRepository<RecordSet, String> {
    RecordSet findByHarvestJobIdAndEndpointJobIdAndStartRecordNumber(String harvestId, long endPointJobId, int start);

    List<RecordSet> findByEndpointJobId(long endPointJobId);

}

