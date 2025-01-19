package com.muzaffar.masjidfinder.bot;

import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface UpdateHandler {
    SendMessage start(Update update, String command);

    List<SendMessage> getMasajid(Update update, String command);

    List<PartialBotApiMethod<?>> sendMasjidLocation(Update update);

    SendMessage closestMasjid(Update update, String command);

    SendMessage mainMenu(Update update, String command);

    SendMessage about(Update update, String command);

    List<SendMessage> favorites(Update update, String command);

    SendMessage temporaryUnavailable(Update update);

    SendMessage notRecognised(Update update);

    SendMessage setMasjidAsFav(Update update, String command);

    SendMessage removeMasjidFromFav(Update update);
}
