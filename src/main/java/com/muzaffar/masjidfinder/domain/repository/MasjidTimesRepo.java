package com.muzaffar.masjidfinder.domain.repository;

import com.muzaffar.masjidfinder.domain.entity.MasjidTimes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MasjidTimesRepo extends JpaRepository<MasjidTimes, Long> {

    List<MasjidTimes> findFirstByMasjidIdOrderByIdDesc(Long masjidId);
}
