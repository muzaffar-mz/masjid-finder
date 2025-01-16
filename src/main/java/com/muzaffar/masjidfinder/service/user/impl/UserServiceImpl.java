package com.muzaffar.masjidfinder.service.user.impl;

import com.muzaffar.masjidfinder.AccessDeniedException;
import com.muzaffar.masjidfinder.bot.model.TgUserDTO;
import com.muzaffar.masjidfinder.domain.entity.User;
import com.muzaffar.masjidfinder.domain.entity.enums.UserStatus;
import com.muzaffar.masjidfinder.domain.repository.UserRepo;
import com.muzaffar.masjidfinder.service.masjid.MasjidService;
import com.muzaffar.masjidfinder.service.user.UserService;
import com.muzaffar.masjidfinder.service.user.mapper.UserMapper;
import com.muzaffar.masjidfinder.service.user.model.UserDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final MasjidService masjidService;

    @Override
    public UserDTO getOrSaveByTgUserDTO(TgUserDTO dto) {

        var user = getOrSaveUser(dto);

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new AccessDeniedException("Access for the user with Telegram id: [%s] is denied".formatted(dto.telegramId()));
        }

        return userMapper.toUserDTO(user);
    }

    @Override
    public UserDTO getUser(TgUserDTO userDTO) {
        var user = getOrSaveUser(userDTO);
        var favs = masjidService.getFavsByUserId(user.getId());
        var defaultOne = masjidService.getDefaultMasjidByUserId(user.getId());
        return new UserDTO(user, defaultOne, favs);
    }

    private User getOrSaveUser(TgUserDTO dto) {
        var user = getUserByTgId(dto.telegramId());

        if (Objects.isNull(user)){
            user = save(dto);
        }
        return user;
    }

    private User getUserByTgId(Long telegramId) {
        return userRepo.findByTelegramId(telegramId).orElse(null);
    }

    private User save(TgUserDTO dto) {
        var user = userMapper.toUser(dto);
        user = userRepo.save(user);
        System.out.println("SUCCESSFULLY SAVED USER: ->" + user);
        return user;
    }
}
