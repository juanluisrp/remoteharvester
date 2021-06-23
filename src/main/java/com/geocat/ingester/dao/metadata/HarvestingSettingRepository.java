package com.geocat.ingester.dao.metadata;

import com.geocat.ingester.model.metadata.HarvesterSetting;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Scope("prototype")
public interface HarvestingSettingRepository extends JpaRepository<HarvesterSetting, Integer> {

    Optional<HarvesterSetting> findByNameAndValue(String name, String value);

    List<HarvesterSetting> findAllByParent(HarvesterSetting parent);
}
