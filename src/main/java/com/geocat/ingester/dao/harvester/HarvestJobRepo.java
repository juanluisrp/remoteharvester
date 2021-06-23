package com.geocat.ingester.dao.harvester;

import com.geocat.ingester.model.harvester.HarvestJob;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Scope("prototype")
public interface HarvestJobRepo extends CrudRepository<HarvestJob, String> {

    @Query("SELECT h FROM HarvestJob h WHERE h.longTermTag = :longTermTag AND h.createTimeUTC = (SELECT max(hs.createTimeUTC) FROM HarvestJob hs WHERE hs.longTermTag = :longTermTag)")
    Optional<HarvestJob> findMostRecentHarvestJobByLongTermTag(@Param("longTermTag") String longTermTag);
}
