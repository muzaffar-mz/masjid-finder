package com.muzaffar.masjidfinder.bot.enums;

import lombok.Getter;

@Getter
public enum Command {

    START("/start"),
    PRAYER_TIMES("🕐 Namoz Vaqtlari"),
    CLOSEST_MASJID("🕌 Yaqin Masjidlar"),
    COMMUNITY_PRAYER_TIMES("⏰ Jamoat Vaqtlari"),
    SHARE_LOCATION("Eng yaqin masjidlarni ko'rish"),
    ABOUT("ℹ️ Bot Haqida"),
    FAVORITES("💚 Mening Masjidlarim"),
//    SETTINGS("Settings"),
//    CHANGE_LANGUAGE("Change bot language"),
//    SET_MASJID("Set/change preferred/default masjid"),
//    SET_CONTACT("Save your contact"),
//    NOTIFICATION("Turn on/off community prayer update notification"),
//    SHARE_CONTACT("Share my contact"),
    BACK("⬅️ Ortga qaytish"),
    OTHER("DIFF"),
    ;


    private final String text;
    Command(String s) {
        this.text = s;
    }

}
