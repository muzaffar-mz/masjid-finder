package com.muzaffar.masjidfinder.bot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdminCallbackCommand {
    //TODO INITIATE THIS ENUM WITH TWO VARIABLES: ONE IS FULL TEXT THE OTHER ONE IS SHORT COMMAND T
    UV_MASJID("UNVERIFIED_MASJID", "UVMD"), // UNVERIFIED MASJID

    OTHER("TEMP_FULL_TEXT", "DIF"),
    UPDATE_MASJID_NAME("Masjid nomini yangilash", "UPMDN"), //UPDATE MASJID NAME
    UPDATE_MASJID_PRAYER_TIME("Jamoat namoz vaqtlarini yangilash", "UPMDPT"), //UPDATE MASJID PRAYER TIMES
    VERIFY_MASJID("Masjidni tasdiqlash", "VYMD"), //VERIFY MASJID
    ;

    private final String fullText;
    private final String text;
}
