package com.muzaffar.masjidfinder.bot.impl;

import com.muzaffar.masjidfinder.bot.AdminUpdateHandler;
import com.muzaffar.masjidfinder.bot.model.TgUserDTO;
import com.muzaffar.masjidfinder.bot.util.AdminKeyboardUtil;
import com.muzaffar.masjidfinder.bot.util.KeyboardUtil;
import com.muzaffar.masjidfinder.bot.util.UpdateUtil;
import com.muzaffar.masjidfinder.service.masjid.MasjidService;
import com.muzaffar.masjidfinder.service.text.TextService;
import com.muzaffar.masjidfinder.service.text.model.TextDTO;
import com.muzaffar.masjidfinder.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;

import static com.muzaffar.masjidfinder.bot.util.UpdateUtil.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUpdateHandlerImpl implements AdminUpdateHandler {

    private final TextService textService;
    private final UserService userService;
    private final MasjidService masjidService;

    @Override
    public SendMessage start(Update update, String command) {
        var textDTO = textService.getAdminText(command);
        return sendMessage(getChatId(update), textDTO, AdminKeyboardUtil.shareContactKB());
    }

    @Override
    public SendMessage registerAdmin(Update update) {
        userService.getOrRegisterSuperAdmin(user(update), update.getMessage().getContact().getPhoneNumber());
        var textDTO = textService.getWelcomeAdminText();
        return sendMessage(getChatId(update), textDTO, AdminKeyboardUtil.defaultSuperAdminKeyboard());
    }

    @Override
    public SendMessage unverifiedMasajidSection(Update update, String command) {
        var textDTO = textService.getAdminText(command);
        return sendMessage(getChatId(update), textDTO, AdminKeyboardUtil.unverifiedMasajidSectonKB());
    }

    @Override
    public List<SendMessage> getAllUnverifiedMasajid(Update update, String command) {
        List<SendMessage> result = new ArrayList<>();
        final var chatId = getChatId(update);

        var masajid = masjidService.getFirst15UnverifiedMasajid();
        StringBuilder sb = new StringBuilder();
        masajid.getFirst().forEach(dto -> {
                    sb.append(dto.getIdAndName()).append("\n");
                });
        sb.append("*Umumiy tasdiqlanmagan masjidlar soni: ").append(masajid.getSecond()).append("*");

        var mainText =  new TextDTO("""
                    *Tasdiqlanmangan masjidlar ro'yxati:* ⬇️ \n
                    """ + sb, true);
        var back = sendMessage(chatId, mainText, KeyboardUtil.backKeyboard());
        result.add(back);

        return result;
    }

    @Override
    public List<PartialBotApiMethod<?>> sendMasjidLocation(Update update) {
        var masjid = masjidService.getMasjid(getMasjidId(update));
        List<PartialBotApiMethod<?>> result = new ArrayList<>();

        var chatId = getChatId(update);
        var sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(masjid.name() + "ga yo'nalish:")
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

    private static TgUserDTO user(Update update) {
        User tgUser = update.hasMessage() ? update.getMessage().getFrom() :
                isCallbackQuery(update) ? update.getCallbackQuery().getFrom() :
                        update.getChannelPost().getFrom();

        return new TgUserDTO(tgUser.getId(), tgUser.getUserName(), tgUser.getFirstName(), tgUser.getLastName());
    }
}
