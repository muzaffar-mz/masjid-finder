package com.muzaffar.masjidfinder.bot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CallbackCommand {
    //TODO INITIATE THIS ENUM WITH TWO VARIABLES: ONE IS FULL TEXT THE OTHER ONE IS SHORT COMMAND T
    BACK_TO_MAIN_MENU("🏠 Asosiy bo'lim","BTMM"), // BACK TO MAIN MENU
    SELECTED_MJ_LOCATION("🕌 {m_name} {km} KM uzoqda 🚶🏽‍♂️", "SMJL"), //SELECTED MASJID LOCATION
    SET_MJ_AS_DEFAULT("TEMP_FULL_TEXT", "SMJADL"), //SETS MASJID AS DEFAULT FOR THE USER

    OTHER("TEMP_FULL_TEXT", "DIF"),
    ;

    private final String fullText;
    private final String text;
}
