package com.muzaffar.masjidfinder.domain.repository;

import com.muzaffar.masjidfinder.domain.entity.Masjid;
import com.muzaffar.masjidfinder.domain.entity.enums.MasjidStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MasjidRepo extends JpaRepository<Masjid, Long> {

    List<Masjid> findAllByIdIn(List<Long> ids);
    List<Masjid> findAllByNameContainingIgnoreCase(String name);
    List<Masjid> findAllByStatusIn(List<MasjidStatus> statuses, Pageable pageable);
    Long countAllByStatusIn(List<MasjidStatus> statuses);
}
