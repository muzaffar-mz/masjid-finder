package com.muzaffar.masjidfinder.domain.repository;

import com.muzaffar.masjidfinder.domain.entity.MetaData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MetaDataRepo extends JpaRepository<MetaData, Long> {

    Optional<MetaData> findAllByKey(String key);
}
