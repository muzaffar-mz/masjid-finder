package com.muzaffar.masjidfinder.domain.repository;

import com.muzaffar.masjidfinder.domain.entity.UserMasjid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserMasjidRepo extends JpaRepository<UserMasjid, Long> {

    List<UserMasjid> findAllByUserId(Long userId);

    Optional<UserMasjid> findByUserIdAndIsDefaultTrue(Long userId);
}
