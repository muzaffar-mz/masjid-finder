package com.muzaffar.masjidfinder.bot.enums;

import lombok.Getter;

@Getter
public enum AdminCommand {

    START("/start"),
    SHARE_CONTACT("☎️Mobil raqamni yuborish"),
    UNVERIFIED_MASAJID("🕌 Tasdiqlanmagan masjidlar"),
    UPDATE_COM_PRAY_TIME("🕐 Jamoat namoz voqtlarini yangilash"),
    SEARCH("🔍 Masjid nomi bilan qidirish"),
    ABOUT("ℹ️ Bot Haqida"),
    BACK("⏮️ Asosiy bo'limga qaytish"),
    TOTAL_UV_LIST("Barcha tasdiqlanmagan masjidlar ro'yxati"),
    SEARCH_UV("Tasdiqlanmagan masjidni qidirish"),
    GET_NEAR_5_UV_MASAJID("Tasdiqlanmagan eng yaqin masjidlar ro'yxati"),

    //    PRAYER_TIMES("🕐 Namoz Vaqtlari"),
    //    COMMUNITY_PRAYER_TIMES("⏰ Jamoat Vaqtlari"),
    //    SHARE_LOCATION("Eng yaqin masjidlarni ko'rish"),
    //    FAVORITES("💚 Mening Masjidlarim"),
    //    GET_THE_CLOSEST_MASAJID("📍 Eng yaqin masjidlarni ko'rish"),
    //    BACK("⏮️ Asosiy bo'limga qaytish")
    //    OTHER("DIFF")
    ;

    private final String text;
    AdminCommand(String s) {
        this.text = s;
    }
}
