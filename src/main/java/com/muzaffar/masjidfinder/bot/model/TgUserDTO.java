package com.muzaffar.masjidfinder.bot.model;

import lombok.NonNull;

public record TgUserDTO(
        Long telegramId,
        String username,
        String firstname,
        String lastname,
        Boolean chatEnabled
) {
    public TgUserDTO(@NonNull Long id, String userName, @NonNull String firstName, String lastName) {
        this(id, userName, firstName, lastName, true);
    }
}
