package com.muzaffar.masjidfinder.bot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdminCallbackCommand {
    //TODO INITIATE THIS ENUM WITH TWO VARIABLES: ONE IS FULL TEXT THE OTHER ONE IS SHORT COMMAND T
    UV_MASJID("Tanlash", "UVMD"), // UNVERIFIED MASJID

    OTHER("TEMP_FULL_TEXT", "DIF"),
    UPDATE_MASJID_NAME("Masjid nomini yangilash", "UPMDN"), //UPDATE MASJID NAME
    UPDATE_MASJID_PRAYER_TIME("Namoz vaqtlarini yangilash", "UPMDPT"), //UPDATE MASJID PRAYER TIMES
    VERIFY_MASJID("Masjidni tasdiqlash", "VYMD"), //VERIFY MASJID
    BACK("Asosiy bo'limga qaytish", "BTMM"), //BACK TO MAIN MENU
    ;

    private final String fullText;
    private final String text;
}
