package com.muzaffar.masjidfinder.service.user.model;

import com.muzaffar.masjidfinder.domain.entity.User;
import com.muzaffar.masjidfinder.service.masjid.model.MasjidDTO;

import java.util.List;

public record UserDTO(
        Long id,
        String phone,
        String email,
        String username,
        String firstname,
        String lastname,
        Long telegramId,
        String password,
        Boolean chatEnabled,
        MasjidDTO defaultMasjidId,
        List<MasjidDTO> masajid
) {

    public UserDTO(User user) {
        this(user.getId(),
                user.getPhone(),
                user.getEmail(),
                user.getUsername(),
                user.getFirstname(),
                user.getLastname(),
                user.getTelegramId(),
                user.getPassword(),
                user.getChat(),
                null,
                null
        );
    }

    public UserDTO(User user, MasjidDTO defaultOne, List<MasjidDTO> masajid) {
        this(user.getId(),
                user.getPhone(),
                user.getEmail(),
                user.getUsername(),
                user.getFirstname(),
                user.getLastname(),
                user.getTelegramId(),
                user.getPassword(),
                user.getChat(),
                defaultOne,
                masajid
        );
    }

    public UserDTO(User user, List<MasjidDTO> masajid) {
        this(user, null, masajid);
    }
}
