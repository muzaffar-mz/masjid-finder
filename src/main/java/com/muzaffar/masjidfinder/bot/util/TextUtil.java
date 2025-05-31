package com.muzaffar.masjidfinder.bot.util;

import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtil {

    private static LocalTime extractTime(String text, String prayerName) {
        Pattern pattern = Pattern.compile(prayerName + "\\s*:\\s*(\\d{2}:\\d{2})", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return LocalTime.parse(matcher.group(1));
        }
        return null;
    }

    public static LocalTime bomdod(String text) {
        return extractTime(text, "Bomdod");
    }

    public static LocalTime peshin(String text) {
        return extractTime(text, "Peshin");
    }

    public static LocalTime asr(String text) {
        return extractTime(text, "Asr");
    }

    public static LocalTime shom(String text) {
        return extractTime(text, "Shom");
    }

    public static LocalTime hufton(String text) {
        return extractTime(text, "Hufton");
    }
}
