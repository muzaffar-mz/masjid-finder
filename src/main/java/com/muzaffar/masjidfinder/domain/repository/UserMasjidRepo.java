package com.muzaffar.masjidfinder.domain.repository;

import com.muzaffar.masjidfinder.domain.entity.UserMasjid;
import com.muzaffar.masjidfinder.domain.entity.enums.UserMasjidType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserMasjidRepo extends JpaRepository<UserMasjid, Long> {

    List<UserMasjid> findAllByUserIdAndDeletedIsFalse(Long userId);

    Optional<UserMasjid> findByUserIdAndIsDefaultTrue(Long userId);

    Optional<UserMasjid> findByUserIdAndMasjidId(Long userId, Long masjidId);

    List<UserMasjid> findAllByUserIdAndType(Long userId, UserMasjidType type);

}

