package com.muzaffar.masjidfinder.domain.repository;

import com.muzaffar.masjidfinder.domain.entity.Masjid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MasjidRepo extends JpaRepository<Masjid, Long> {

    List<Masjid> findAllByIdIn(List<Long> ids);
}
