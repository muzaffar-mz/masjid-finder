package com.muzaffar.masjidfinder.service.user.impl;

import com.muzaffar.masjidfinder.AccessDeniedException;
import com.muzaffar.masjidfinder.bot.model.TgUserDTO;
import com.muzaffar.masjidfinder.domain.entity.User;
import com.muzaffar.masjidfinder.domain.entity.enums.UserRole;
import com.muzaffar.masjidfinder.domain.entity.enums.UserStatus;
import com.muzaffar.masjidfinder.domain.repository.MetaDataRepo;
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
    private final MetaDataRepo metaDataRepo;

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

    @Override
    public UserDTO getOrRegisterSuperAdmin(TgUserDTO tgUser, String phoneNumber) {
        log.info("Get or Register Super Admin --> TG user: {}, phoneNumber: {}", tgUser, phoneNumber);
        if (phoneNumber.startsWith("+")) {
            phoneNumber = phoneNumber.replace("+", "");
        }
        // first we check meta whether he has access to the admin bot
        var listOfMeta = metaDataRepo.findAllByKey("ADMIN_REGISTRATION");
        String finalPhoneNumber = phoneNumber;
        var isAllowed = listOfMeta.stream()
                .anyMatch(meta -> meta.getValue().equals(finalPhoneNumber));
        if (!isAllowed) {
            throw new AccessDeniedException("Access for the user with phoneNumber [%s] is denided".formatted(phoneNumber));
        }

        var user = getOrSaveUser(tgUser);
        user.setPhone(phoneNumber);
        user.setRole(UserRole.SUPER_ADMIN);
        userRepo.save(user);

        return userMapper.toUserDTO(user);
    }

    @Override
    public boolean isUserAdmin(TgUserDTO user) {
        var admin = userRepo.findByTelegramId(user.telegramId()).orElse(null);
        return Objects.nonNull(admin) && (admin.getRole() == UserRole.SUPER_ADMIN || admin.getRole() == UserRole.ADMIN);
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
