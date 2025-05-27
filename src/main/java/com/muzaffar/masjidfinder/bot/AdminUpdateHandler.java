package com.muzaffar.masjidfinder.bot;

import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface AdminUpdateHandler {
    SendMessage start(Update update, String command);

    SendMessage registerAdmin(Update update);

    SendMessage unverifiedMasajidSection(Update update, String command);

    List<SendMessage> getAllUnverifiedMasajid(Update update, String command);

    List<PartialBotApiMethod<?>> sendMasjidLocation(Update update);

    SendMessage mainMenu(Update update, String command);

    SendMessage searchUVMasjidButton(Update update, String command);

    List<SendMessage> findUVMasjidByName(Update update, String command);

    SendMessage getMasjidUpdateService(Update update);

    SendMessage preUpdateMasjidName(Update update);

    SendMessage updateMasjidName(Update update, String command);

    SendMessage verifyMasjid(Update update);

    SendMessage preUpdatePrayerTimes(Update update);

    SendMessage updatePrayerTimes(Update update, String command);
}
