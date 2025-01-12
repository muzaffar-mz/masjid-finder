package com.muzaffar.masjidfinder.domain.repository;

import com.muzaffar.masjidfinder.bot.enums.Command;
import com.muzaffar.masjidfinder.domain.entity.Text;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TextRepo extends JpaRepository<Text, Long> {
    Optional<Text> findByCommand(Command command);
}
