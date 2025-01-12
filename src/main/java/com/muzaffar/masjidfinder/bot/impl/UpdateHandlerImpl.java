package com.muzaffar.masjidfinder.bot.impl;

import com.muzaffar.masjidfinder.bot.UpdateHandler;
import com.muzaffar.masjidfinder.bot.model.TgUserDTO;
import com.muzaffar.masjidfinder.bot.util.KeyboardUtil;
import com.muzaffar.masjidfinder.bot.util.UpdateUtil;
import com.muzaffar.masjidfinder.model.LocationDTO;
import com.muzaffar.masjidfinder.service.masjid.MasjidService;
import com.muzaffar.masjidfinder.service.masjid.model.MasjidDTO;
import com.muzaffar.masjidfinder.service.text.TextService;
import com.muzaffar.masjidfinder.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;

import static com.muzaffar.masjidfinder.bot.util.UpdateUtil.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateHandlerImpl implements UpdateHandler {

    private final UserService userService;
    private final MasjidService masjidService;
    private final TextService textService;

    @Override
    public SendMessage start(Update update, String command) {
        userService.getOrSaveByTgUserDTO(user(update));

        var textDTO = textService.getText(command);

        return sendMessage(getChatId(update), textDTO, KeyboardUtil.defaultKeyboard());
    }

    @Override
    public SendMessage closestMasjid(Update update, String command) {
        var textDTO = textService.getText(command);
        return sendMessage(getChatId(update), textDTO, KeyboardUtil.getLocationKB());
    }

    @Override
    public List<SendMessage> getMasajid(Update update) {
        List<SendMessage> result = new ArrayList<>();
        final var chatId = getChatId(update);
        final String mainText = "*Iltimos qulay masjidni tanlang:*";
        SendMessage m = message(chatId, mainText);
        m.enableMarkdownV2(true);
        result.add(m);

        final var location = update.getMessage().getLocation();
        var masajid = masjidService.getMasajidClosestToLocation(new LocationDTO(location.getLatitude(), location.getLongitude()));
        for (MasjidDTO masjid : masajid) {
            final String text = masjid.getNameAndPrayerTimesForBot();
            var message = UpdateUtil.sendMessage(chatId, text, KeyboardUtil.getMasjidKeyboard(masjid));
            message.enableMarkdownV2(true);
            result.add(message);
        }

//        //back button
        var backButton = UpdateUtil.sendMessage(chatId, "⏮️ *Asosiy bo'limga qaytish:* ", KeyboardUtil.getInlineMainMenuButton());
        backButton.enableMarkdownV2(true);
        result.add(backButton);
        return result;
    }

    @Override
    public List<PartialBotApiMethod<?>> sendMasjidLocation(Update update) {
        var masjid = masjidService.getMasjid(getMasjidId(update));
        List<PartialBotApiMethod<?>> result = new ArrayList<>();

        var chatId = getChatId(update);
        var sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(masjid.name())
                .build();
        result.add(sendMessage);

        var sendLocation =  SendLocation
                .builder()
                .replyMarkup(KeyboardUtil.getLocationKB())
                .longitude(masjid.longitude())
                .latitude(masjid.latitude())
                .chatId(chatId)
                .build();
        result.add(sendLocation);

        return result;
    }

    public static TgUserDTO user(Update update) {
        User tgUser = update.hasMessage() ? update.getMessage().getFrom() :
                isCallbackQuery(update) ? update.getCallbackQuery().getFrom() :
                        update.getChannelPost().getFrom();

        return new TgUserDTO(tgUser.getId(), tgUser.getUserName(), tgUser.getFirstName(), tgUser.getLastName());
    }
}
