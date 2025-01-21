package com.muzaffar.masjidfinder.bot.impl;

import com.muzaffar.masjidfinder.bot.UpdateHandler;
import com.muzaffar.masjidfinder.bot.enums.Command;
import com.muzaffar.masjidfinder.bot.model.TgUserDTO;
import com.muzaffar.masjidfinder.bot.util.KeyboardUtil;
import com.muzaffar.masjidfinder.bot.util.UpdateUtil;
import com.muzaffar.masjidfinder.domain.repository.UserRepo;
import com.muzaffar.masjidfinder.model.LocationDTO;
import com.muzaffar.masjidfinder.service.masjid.MasjidService;
import com.muzaffar.masjidfinder.service.masjid.model.MasjidDTO;
import com.muzaffar.masjidfinder.service.text.TextService;
import com.muzaffar.masjidfinder.service.text.model.TextDTO;
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
import java.util.Optional;

import static com.muzaffar.masjidfinder.bot.util.UpdateUtil.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateHandlerImpl implements UpdateHandler {

    private final UserService userService;
    private final MasjidService masjidService;
    private final TextService textService;
    private final UserRepo userRepo;

    @Override
    public SendMessage start(Update update, String command) {
        userService.userUpdateChatsState(update, false);
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
    public List<SendMessage> getMasajid(Update update, String command) {
        List<SendMessage> result = new ArrayList<>();
        final var chatId = getChatId(update);

        //TODO
        var mainText = textService.getText("location");
        SendMessage m = sendMessage(chatId, mainText, KeyboardUtil.backKeyboard());
        result.add(m);

        final var location = update.getMessage().getLocation();
        var masajid = masjidService.getMasajidClosestToLocation(new LocationDTO(location.getLatitude(), location.getLongitude()));
        for (MasjidDTO masjid : masajid) {
            final String text = masjid.getNameAndPrayerTimesForBot();
            var message = UpdateUtil.sendMessage(chatId, text, KeyboardUtil.getMasjidKeyboardV2(masjid));
            message.enableMarkdownV2(true);
            result.add(message);
        }

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

    @Override
    public SendMessage mainMenu(Update update, String command) {
        var textDTO = textService.getText(command);
        return sendMessage(getChatId(update), textDTO, KeyboardUtil.defaultKeyboard());
    }

    @Override
    public SendMessage about(Update update, String command) {
        var textDTO = textService.getText(command);
        return sendMessage(getChatId(update), textDTO, KeyboardUtil.defaultKeyboard());
    }

    @Override
    public List<SendMessage> favorites(Update update, String command) {
        List<SendMessage> result = new ArrayList<>();
        final var chatId = getChatId(update);

        var mainText = textService.getText(command);
        var user = userService.getUser(user(update));

        if (user.masajid().isEmpty()) {
            mainText = new TextDTO(
                    mainText.text() + "\n *Sizda hali masjidlar bu ro'yxatga qo'shilmagan*",
                    mainText.isFormatted()
            );
        }

        SendMessage main = sendMessage(chatId, mainText, KeyboardUtil.backKeyboard());
        result.add(main);

        for (MasjidDTO dto : user.masajid()) {
            final String text = dto.getNameAndPrayerTimesForBot();
            var message = UpdateUtil.sendMessage(chatId, text, KeyboardUtil.getMasjidKeyboardV3(dto));
            message.enableMarkdownV2(true);
            result.add(message);
        }

        return result;
    }

    @Override
    public List<PartialBotApiMethod<?>> findMasjidByName(Update update) {
        List<PartialBotApiMethod<?>> returnList = new ArrayList<>();
        Long chatId = Long.valueOf(getChatId(update));
        String masjidName = update.getMessage().getText(); // Get the masjid name

        var masajid = masjidService.getMasjidByName(masjidName);
        if (masajid.isEmpty()) {
            returnList.add(SendMessage.builder()
                    .chatId(chatId)
                    .text("\n *Masjid nomida hatolik yoki siz kiritgan masjid ma'lumoti mavjud emas.* ")
                    .replyMarkup(KeyboardUtil.defaultKeyboard())
                    .build());
            return returnList;
        }


        for (MasjidDTO masjid : masajid) {
            returnList.add(SendMessage.builder()
                    .chatId(chatId)
                    .text(masjid.getNameAndPrayerTimesForBot())
                    .replyMarkup(KeyboardUtil.defaultKeyboard())
                    .build());

            if (masjid.latitude() != null && masjid.longitude() != null) {
                returnList.add(SendLocation.builder()
                        .chatId(chatId)
                        .latitude(masjid.latitude())
                        .longitude(masjid.longitude())
                        .build());
            } else {
                returnList.add(SendMessage.builder()
                        .chatId(chatId)
                        .text("\n *Masjid nomida hatolik yoki siz kiritgan masjid manzili mavjud emas.* " + masjid.name())
                        .build());
            }
        }

        return returnList;
    }

    @Override
    public void enableMasjidNameInput(Update update) {
        userService.userUpdateChatsState(update, true);
    }


    @Override
    public SendMessage temporaryUnavailable(Update update) {

        var textDTO = textService.temporaryUnavailable();

        return sendMessage(getChatId(update), textDTO, KeyboardUtil.defaultKeyboard());
    }

    @Override
    public List<PartialBotApiMethod<?>> notRecognised(Update update) {
        Long userId = user(update).telegramId();
        List<PartialBotApiMethod<?>> responseList = new ArrayList<>();

        if (userService.isChatEnabled(userId)) {
            List<PartialBotApiMethod<?>> response = findMasjidByName(update);

            userService.userUpdateChatsState(update, false);
            responseList.addAll(response);

            return responseList;
        }
        var textDTO = textService.unrecognised();
       responseList.add(sendMessage(getChatId(update), textDTO, KeyboardUtil.defaultKeyboard()));
       return responseList;
    }

    public static TgUserDTO user(Update update) {
        User tgUser = update.hasMessage() ? update.getMessage().getFrom() :
                isCallbackQuery(update) ? update.getCallbackQuery().getFrom() :
                        update.getChannelPost().getFrom();

        return new TgUserDTO(tgUser.getId(), tgUser.getUserName(), tgUser.getFirstName(), tgUser.getLastName());
    }
}
