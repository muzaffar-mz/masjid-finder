package com.muzaffar.masjidfinder.bot.model;

public record TgUserDTO(
        Long telegramId,
        String username,
        String firstname,
        String lastname
) {
}
