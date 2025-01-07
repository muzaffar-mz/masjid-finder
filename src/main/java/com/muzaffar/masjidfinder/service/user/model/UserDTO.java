package com.muzaffar.masjidfinder.service.user.model;

import com.muzaffar.masjidfinder.domain.entity.User;

public record UserDTO(
        Long id,
        String phone,
        String email,
        String username,
        String firstname,
        String lastname,
        Long telegramId,
        String password,
        Long defaultMasjidId
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
                user.getDefaultMasjid() != null ? user.getDefaultMasjid().getId() : null
        );
    }
}
