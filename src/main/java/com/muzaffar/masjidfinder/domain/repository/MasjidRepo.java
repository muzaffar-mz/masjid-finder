package com.muzaffar.masjidfinder.domain.repository;

import com.muzaffar.masjidfinder.domain.entity.Masjid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MasjidRepo extends JpaRepository<Masjid, Long> {
}
