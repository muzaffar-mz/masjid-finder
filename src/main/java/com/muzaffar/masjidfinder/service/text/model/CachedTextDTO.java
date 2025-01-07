package com.muzaffar.masjidfinder.service.text.model;

import java.time.LocalDateTime;

public record CachedTextDTO(
        String text,
        Boolean isFormatted,
        LocalDateTime expiration
) {
}
