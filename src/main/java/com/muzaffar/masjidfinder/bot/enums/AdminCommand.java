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
    LOCATION("\uD83D\uDCCD Joylashuvni yuborish"),
    GET_ASSIGNED_MASJID("Biriktirilgan masjidni ko'rsatish"),
    SEARCH_BY_ID("🔍 Masjid ID-si bilan qidirish"),
    GET_NEAR_5_MASAJID("Eng yaqin masjidlarni ko'rstish"),

    //    PRAYER_TIMES("🕐 Namoz Vaqtlari"),
    //    COMMUNITY_PRAYER_TIMES("⏰ Jamoat Vaqtlari"),
    //    SHARE_LOCATION("Eng yaqin masjidlarni ko'rish"),
    //    FAVORITES("💚 Mening Masjidlarim"),
    //    GET_THE_CLOSEST_MASAJID("📍 Eng yaqin masjidlarni ko'rish")
    //    OTHER("DIFF")
    ;

    private final String text;
    AdminCommand(String s) {
        this.text = s;
    }
}
