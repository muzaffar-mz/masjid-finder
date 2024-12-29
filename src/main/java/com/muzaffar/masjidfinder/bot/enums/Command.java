package com.muzaffar.masjidfinder.bot.enums;

import lombok.Getter;

@Getter
public enum Command {
    START("/start"),
    PRAYER_TIME("Prayer Time"),
    CLOSEST_MASJID("Closest Masjid"),
    SHARE_LOCATION("Share my location"),
    SETTINGS("Settings"),
    CHANGE_LANGUAGE("Change bot language"),
    SET_MASJID("Set/change preferred/default masjid"),
    SET_CONTACT("Save your contact"),
    NOTIFICATION("Turn on/off community prayer update notification"),
    SHARE_CONTACT("Share my contact"),
    BACK("Back"),
    OTHER("DIFF"),
    ;


    private final String text;
    Command(String s) {
        this.text = s;
    }

}
