package com.muzaffar.masjidfinder.domain.repository;

import com.muzaffar.masjidfinder.AbstractTestcontainers;
import com.muzaffar.masjidfinder.domain.entity.User;
import com.muzaffar.masjidfinder.domain.entity.enums.UserRole;
import com.muzaffar.masjidfinder.domain.entity.enums.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepoTest extends AbstractTestcontainers {

    @Autowired
    private UserRepo underTest;


    @Test
    void findByTelegramId() {
        // Given
        var tgID = 1L;
        var user = new User();
        user.setTelegramId(tgID);
        user.setUsername("@test_user_name");
        user.setFirstname("First_name");
        user.setLastname("Last_name");
        user.setChat(false);
        user.setStatus(UserStatus.ACTIVE);
        user.setRole(UserRole.USER);

        underTest.save(user);

        // When
        Optional<User> actual = underTest.findByTelegramId(tgID);

        //Then
        assertThat(actual).isPresent();
    }

}