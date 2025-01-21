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
    GET_THE_CLOSEST_MASAJID("📍 Eng yaqin masjidlarni ko'rish"),
    FIND_BY_MASJID_NAME("\uD83D\uDD0E Masjidni izlash"),
    BACK("⏮️ Asosiy bo'limga qaytish"),
    OTHER("DIFF"),
    ;


    private final String text;
    Command(String s) {
        this.text = s;
    }

}
