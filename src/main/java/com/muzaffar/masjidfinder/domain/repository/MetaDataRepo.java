package com.muzaffar.masjidfinder.domain.repository;

import com.muzaffar.masjidfinder.domain.entity.MetaData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MetaDataRepo extends JpaRepository<MetaData, Long> {

    List<MetaData> findAllByKey(String key);
}
