package com.muzaffar.masjidfinder.bot;

import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface UpdateHandler {
    SendMessage start(Update update);
    SendMessage getMasjids(Update update);
    DeleteMessage deleteMessage(Update update);
    List<PartialBotApiMethod<?>> sendMasjidLocation(Update update);
}
