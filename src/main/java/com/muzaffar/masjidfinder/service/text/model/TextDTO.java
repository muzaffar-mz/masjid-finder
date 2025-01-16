package com.muzaffar.masjidfinder.service.text.model;

public record TextDTO(
        String text,
        Boolean isFormatted
) {
    public TextDTO(String text) {
        this(text, false); // Default value for isFormatted
    }
}
