package com.muzaffar.masjidfinder.service.user.impl;

import com.muzaffar.masjidfinder.bot.model.TgUserDTO;
import com.muzaffar.masjidfinder.domain.entity.User;
import com.muzaffar.masjidfinder.domain.entity.enums.UserStatus;
import com.muzaffar.masjidfinder.domain.repository.UserRepo;
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

    @Override
    public UserDTO getOrSave(TgUserDTO dto) {

        var user = getUserByTgId(dto.telegramId());

        if (Objects.nonNull(user)){
            if (user.getStatus() != UserStatus.ACTIVE) {
                //TODO throw exception
                return null;
            }

            return userMapper.toUserDTO(user);
        }

        return userMapper.toUserDTO(save(dto));
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
