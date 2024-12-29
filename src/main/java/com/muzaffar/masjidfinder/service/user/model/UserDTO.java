package com.muzaffar.masjidfinder.service.user.model;

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
}
