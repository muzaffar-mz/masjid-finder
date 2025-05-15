package com.muzaffar.masjidfinder.service.text.model;

import com.muzaffar.masjidfinder.domain.entity.Text;

import java.time.LocalDateTime;

public record TextDTO(
        String text,
        Boolean isFormatted,
        LocalDateTime expiry
) {

    public TextDTO(String text, Boolean isFormatted) {
        this(text, isFormatted, null); // Default value for isFormatted
    }

    public TextDTO(Text text) {
        this(text.getMessage(), text.getIsFormatted(), LocalDateTime.now().plusDays(30));
    }

}
