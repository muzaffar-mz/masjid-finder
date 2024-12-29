package com.muzaffar.masjidfinder.domain.repository;

import com.muzaffar.masjidfinder.domain.entity.User;
import com.muzaffar.masjidfinder.domain.entity.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByPhone(String phone);
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndRole(String email, UserRole role);

    Optional<User> findByTelegramId(Long telegramId);
}
