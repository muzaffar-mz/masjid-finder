package com.muzaffar.masjidfinder.bot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CallbackCommand {
    //TODO INITIATE THIS ENUM WITH TWO VARIABLES: ONE IS FULL TEXT THE OTHER ONE IS SHORT COMMAND T
    BACK_TO_MAIN_MENU("🏠 Asosiy bo'lim","BTMM"), // BACK TO MAIN MENU
    SELECTED_MJ_LOCATION("📍 {km} KM uzoqda. Yo'nalishni olish", "SMJL"), //SELECTED MASJID LOCATION
    SET_MJ_AS_DEFAULT("TEMP_FULL_TEXT", "SMJADL"), //SETS MASJID AS DEFAULT FOR THE USER
    SET_MJ_AS_FAV("💚 «Mening Masjidlarim»ga qo'shish", "SMJAFAV"),
    REMOVE_FROM_MJ_AS_FAV("🩶 «Mening Masjidlarim»dan chiqarish", "RFMJAF"),

    OTHER("TEMP_FULL_TEXT", "DIF"),
    ;

    private final String fullText;
    private final String text;
}
