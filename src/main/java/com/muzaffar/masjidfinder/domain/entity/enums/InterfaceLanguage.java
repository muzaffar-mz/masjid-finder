package com.muzaffar.masjidfinder.domain.entity.enums;

import lombok.Getter;

@Getter
public enum InterfaceLanguage {
    UZBEK(0),
    RUSSIAN(1),
    ENGLISH(2);

    private final int language;
    InterfaceLanguage(int language) {
        this.language = language;
    }

    public static InterfaceLanguage getInterfaceLanguage(int index) {
        return switch (index) {
            case 0 -> UZBEK;
            case 1 -> RUSSIAN;
            case 2 -> ENGLISH;
            default -> throw new IllegalArgumentException("Invalid index: " + index);
        };
    }
}
