package com.muzaffar.masjidfinder.domain.repository;

import com.muzaffar.masjidfinder.domain.entity.User;
import com.muzaffar.masjidfinder.domain.entity.enums.UserRole;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByTelegramId(Long telegramId);
    @NotNull Optional<User> findById(@NotNull Long id);
}
