package com.muzaffar.masjidfinder.bot.impl;

import com.muzaffar.masjidfinder.bot.UpdateHandler;
import com.muzaffar.masjidfinder.bot.model.TgUserDTO;
import com.muzaffar.masjidfinder.bot.util.KeyboardUtil;
import com.muzaffar.masjidfinder.bot.util.UpdateUtil;
import com.muzaffar.masjidfinder.model.LocationDTO;
import com.muzaffar.masjidfinder.service.masjid.MasjidService;
import com.muzaffar.masjidfinder.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
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

    @Override
    public SendMessage start(Update update) {
        var user = userService.getOrSave(user(update));

        //TODO the message service
        final String welcome = "Hurmatli " + user.firstname() + "\\! `Masjid Sari` botimizga xush kelibsiz\\!" +
                "\nEng yaqin Masjid Sari borish uchun joylashuvni yuboring";

        //TODO the getting
        var message = UpdateUtil.getSendMessage(getChatId(update), welcome, KeyboardUtil.getLocationKB());
        message.enableMarkdownV2(true);
        return message;
    }

    @Override
    public SendMessage getMasjids(Update update) {
        final String text = "Iltimos qulay masjidni tanlang:";

        final var location = update.getMessage().getLocation();
        var masjids = masjidService.getMasjidsClosestToLocation(new LocationDTO(location.getLatitude(), location.getLongitude()));
        return UpdateUtil.inLineKeyboard(getChatId(update), text, KeyboardUtil.getMasjidsKeyboard(masjids));
    }

    @Override
    public DeleteMessage deleteMessage(Update update) {
        return new DeleteMessage(getChatId(update), messageId(update));
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
