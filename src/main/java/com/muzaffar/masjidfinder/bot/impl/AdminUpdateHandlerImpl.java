package com.muzaffar.masjidfinder.bot.impl;

import com.muzaffar.masjidfinder.bot.AdminUpdateHandler;
import com.muzaffar.masjidfinder.bot.model.TgUserDTO;
import com.muzaffar.masjidfinder.bot.util.AdminKeyboardUtil;
import com.muzaffar.masjidfinder.bot.util.KeyboardUtil;
import com.muzaffar.masjidfinder.bot.util.TextUtil;
import com.muzaffar.masjidfinder.domain.entity.enums.MasjidStatus;
import com.muzaffar.masjidfinder.service.cache.CacheService;
import com.muzaffar.masjidfinder.service.masjid.MasjidService;
import com.muzaffar.masjidfinder.service.masjid.model.MasjidDTO;
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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

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
    private final CacheService cacheService;

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
        var back = sendMessage(chatId, mainText, AdminKeyboardUtil.backKB());
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

    @Override
    public SendMessage mainMenu(Update update, String command) {
        cacheService.adminToGeneralMode(getChatId(update));
        var textDTO = textService.getAdminMainMenu();
        return sendMessage(getChatId(update), textDTO, AdminKeyboardUtil.defaultSuperAdminKeyboard());
    }

    @Override
    public SendMessage searchUVMasjidButton(Update update, String command) {
        cacheService.adminToSearchMode(getChatId(update));
        var textDTO = textService.getAdminSearchMasjidText();
        return sendMessage(getChatId(update), textDTO, AdminKeyboardUtil.backKB());
    }

    @Override
    public List<SendMessage> findUVMasjidByName(Update update, String name) {
        cacheService.adminToGeneralMode(getChatId(update));
        List<SendMessage> result = new ArrayList<>();
        var chatId = getChatId(update);
        var masajid = masjidService.findUVMasajidByName(name);
        if (masajid.isEmpty()) {
            var textDTO = textService.getNoMasajidFoundText();
            var sendMessage = sendMessage(chatId, textDTO, AdminKeyboardUtil.backKB());
            result.add(sendMessage);
            return result;
        }

        var firstText = textService.getFoundMasajidText();
        result.add(sendMessage(chatId, firstText, AdminKeyboardUtil.backKB()));

        masajid.forEach(m -> {
            final String text = m.getIdAndName();
            var message = sendMessage(chatId, text, AdminKeyboardUtil.getInlineUVMasjidKeyboard(m));
            message.enableMarkdownV2(true);
            result.add(message);
        });
        var lastText = textService.getYouCanTryAgain();
        result.add(message(chatId, lastText));
        return result;
    }

    @Override
    public SendMessage getMasjidUpdateService(Update update) {
        var masjid = masjidService.getMasjid(getMasjidId(update));
        var isVerified = masjid.status() == MasjidStatus.CONFIRMED;
        return sendMessage(getChatId(update), masjid.getIdAndName(), AdminKeyboardUtil.getInlineMasjidUpdateKeyboard(masjid, isVerified));
    }

    @Override
    public SendMessage preUpdateMasjidName(Update update) {
        final String chatId = getChatId(update);
        final Long masjidId = getMasjidId(update);
        cacheService.adminToUpdateMasjidNameMode(chatId, masjidId);
        var masjid = masjidService.getMasjid(masjidId);
        final var text = textService.getEnterUpdatedMasjidName(masjid.getIdAndName());
        return sendMessage(chatId, text, AdminKeyboardUtil.backKB());
    }

    @Override
    public SendMessage updateMasjidName(Update update, String masjidName) {
        final String chatId = getChatId(update);
        final var masjidId = cacheService.getMasjidIdToUpdate(chatId);
        masjidService.updateMasjidName(user(update), masjidId, masjidName);
        final var text = textService.masjidNameSuccessfullyUpdated(masjidName);
        return sendMessage(chatId, text, AdminKeyboardUtil.backKB());
    }

    @Override
    public SendMessage verifyMasjid(Update update) {
        final var chatId = getChatId(update);
        final var masjidId = getMasjidId(update);
        var masjid = masjidService.verifyMasjidById(user(update), masjidId);
        final var text = textService.masjidIsVerified(masjid.getIdAndName());
        return sendMessage(chatId, text, AdminKeyboardUtil.backKB());
    }

    @Override
    public SendMessage preUpdatePrayerTimes(Update update) {
        final var chatId= getChatId(update);
        final var masjidId = getMasjidId(update);
        cacheService.adminToUpdatePrayerTimesMode(chatId, masjidId);
        final var text = textService.updatePrayerTimesSample();
        return sendMessage(chatId, text, AdminKeyboardUtil.backKB());
    }

    @Override
    public SendMessage updatePrayerTimes(Update update, String command) {
        final var chatId = getChatId(update);
        final var user = user(update);
        var masjidId = cacheService.getMasjidIdToUpdate(chatId);
        var masjid = masjidService.updateMasjidPrayerTimes(user, masjidId,
                TextUtil.bomdod(command), TextUtil.peshin(command), TextUtil.asr(command),
                TextUtil.shom(command), TextUtil.hufton(command));
        final var text = new TextDTO(masjid.getNameAndPrayerTimesForBot(), true);
        return sendMessage(chatId, text, AdminKeyboardUtil.backKB());
    }

    private static TgUserDTO user(Update update) {
        User tgUser = update.hasMessage() ? update.getMessage().getFrom() :
                isCallbackQuery(update) ? update.getCallbackQuery().getFrom() :
                        update.getChannelPost().getFrom();

        return new TgUserDTO(tgUser.getId(), tgUser.getUserName(), tgUser.getFirstName(), tgUser.getLastName());
    }

}
