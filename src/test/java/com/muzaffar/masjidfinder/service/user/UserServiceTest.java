package com.muzaffar.masjidfinder.service.user;

import com.muzaffar.masjidfinder.AccessDeniedException;
import com.muzaffar.masjidfinder.bot.model.TgUserDTO;
import com.muzaffar.masjidfinder.domain.entity.User;
import com.muzaffar.masjidfinder.domain.entity.enums.UserRole;
import com.muzaffar.masjidfinder.domain.entity.enums.UserStatus;
import com.muzaffar.masjidfinder.domain.repository.UserRepo;
import com.muzaffar.masjidfinder.service.user.impl.UserServiceImpl;
import com.muzaffar.masjidfinder.service.user.mapper.UserMapper;
import com.muzaffar.masjidfinder.service.user.model.UserDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService underTest;

    @Mock
    private UserRepo userRepo;
    @Mock
    private UserMapper userMapper;

    //DUMMY Variables
    private TgUserDTO tgUser;
    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        underTest = new UserServiceImpl(userRepo, userMapper);
        this.tgUser = new TgUserDTO(15L, "@user_name", "First_Name", "Last_Name");

        this.user = new User();
        user.setRole(UserRole.USER);
        user.setStatus(UserStatus.ACTIVE);
        user.setTelegramId(15L);
        user.setUsername("@user_name");
        user.setFirstname("First_Name");
        user.setLastname("Last_Name");

        this.userDTO = new UserDTO(user);
    }


    /** Three cases:
     *         1. Saves the user
     *         2. Gets the exiisting user
     *         3. Throws exception FORBIDDEN
     */

    @Test
    void getsUser() {
        // Given
        when(userRepo.findByTelegramId(this.tgUser.telegramId())).thenReturn(Optional.of(this.user));
        when(userMapper.toUserDTO(this.user)).thenReturn(this.userDTO);

        // When
        var actual = underTest.getOrSaveByTgUserDTO(tgUser);

        // Then
        assertThat(actual).isEqualTo(userDTO);
    }

    @Test
    void savesUser() {
        // Given
        when(userRepo.findByTelegramId(this.tgUser.telegramId())).thenReturn(Optional.empty());
        when(userMapper.toUser(this.tgUser)).thenReturn(this.user);
        when(userRepo.save(this.user)).thenReturn(this.user);
        when(userMapper.toUserDTO(this.user)).thenReturn(this.userDTO);

        // When
        var actual = underTest.getOrSaveByTgUserDTO(this.tgUser);

        //Then
        assertThat(actual).isEqualTo(this.userDTO);
    }

    @Test
    void willThrowWhenUserIsBlocked() {
        // Given
        var user = this.user;
        user.setStatus(UserStatus.BLOCKED);
        when(userRepo.findByTelegramId(this.tgUser.telegramId())).thenReturn(Optional.of(user));

        // When
        //Then
        assertThatThrownBy(() -> underTest.getOrSaveByTgUserDTO(this.tgUser))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("Access for the user with Telegram id: [%s] is denied".formatted(tgUser.telegramId()));
    }
}