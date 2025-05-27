package com.muzaffar.masjidfinder.bot.util;

import com.muzaffar.masjidfinder.bot.enums.AdminCallbackCommand;
import com.muzaffar.masjidfinder.bot.enums.AdminCommand;
import com.muzaffar.masjidfinder.bot.enums.CallbackCommand;
import com.muzaffar.masjidfinder.service.masjid.model.MasjidDTO;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class AdminKeyboardUtil {


    public static ReplyKeyboardMarkup shareContactKB() {
        return contactButton.andThen(buttonRow)
                .andThen(replyKeyboard)
                .apply(AdminCommand.SHARE_CONTACT);

    }

    public static ReplyKeyboardMarkup defaultSuperAdminKeyboard() {
        return replyKeyboardMultiRow.apply(
                List.of(
                        button.andThen(buttonRow).apply(AdminCommand.UNVERIFIED_MASAJID),
                        button.andThen(buttonRow).apply(AdminCommand.UPDATE_COM_PRAY_TIME),
                        button.andThen(buttonRow).apply(AdminCommand.ABOUT)
                )
        );
    }

    public static InlineKeyboardMarkup getInlineUVMasjidKeyboard(MasjidDTO masjid) {
        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(new InlineKeyboardRow(masjidInlineKeyboardButton.apply(masjid))))
                .build();
    }

    public static InlineKeyboardMarkup getInlineMasjidUpdateKeyboard(MasjidDTO masjid, Boolean isVerified) {
        return InlineKeyboardMarkup.builder()
                .keyboard(masjidUpdateILKRows.apply(masjid, isVerified))
                .build();
    }



    static Function<MasjidDTO, InlineKeyboardButton> masjidILKUpdateNameButton = masjid ->
            InlineKeyboardButton.builder()
                    .text(AdminCallbackCommand.UPDATE_MASJID_NAME.getFullText())
                    .callbackData(AdminCallbackCommand.UPDATE_MASJID_NAME.getText() + "_" + masjid.id())
                    .build();

    static Function<MasjidDTO, InlineKeyboardButton> masjidILKUpdatePrayerTimes = masjid ->
            InlineKeyboardButton.builder()
                    .text(AdminCallbackCommand.UPDATE_MASJID_PRAYER_TIME.getFullText())
                    .callbackData(AdminCallbackCommand.UPDATE_MASJID_PRAYER_TIME.getText() + "_" + masjid.id())
                    .build();

    static Function<MasjidDTO, InlineKeyboardButton> masjidILKVerifyMasjidButon = masjid ->
            InlineKeyboardButton.builder()
                    .text(AdminCallbackCommand.VERIFY_MASJID.getFullText())
                    .callbackData(AdminCallbackCommand.VERIFY_MASJID.getText() + "_" + masjid.id())
                    .build();

    static Function<MasjidDTO, InlineKeyboardButton> masjidInlineKeyboardButton =
            masjid -> InlineKeyboardButton.builder()
                    .text(masjid.name())
                    .callbackData(AdminCallbackCommand.UV_MASJID.getText() + "_" + masjid.id())
                    .build();


    public static ReplyKeyboardMarkup unverifiedMasajidSectonKB() {
        return replyKeyboardMultiRow.apply(
                List.of(
                        button.andThen(buttonRow).apply(AdminCommand.TOTAL_UV_LIST),
                        button.andThen(buttonRow).apply(AdminCommand.SEARCH_UV),
                        button.andThen(buttonRow).apply(AdminCommand.GET_NEAR_5_UV_MASAJID),
                        button.andThen(buttonRow).apply(AdminCommand.BACK)
                )
        );
    }

    public static ReplyKeyboardMarkup backKB() {
        return replyKeyboard.apply(
                button.andThen(buttonRow).apply(AdminCommand.BACK)
        );
    }

    static BiFunction<MasjidDTO, Boolean, List<InlineKeyboardRow>> masjidUpdateILKRows = (masjid, isVerified) -> {
        List<InlineKeyboardRow> res = new ArrayList<>();
        res.add(new InlineKeyboardRow(masjidILKUpdateNameButton.apply(masjid)));
        res.add(new InlineKeyboardRow(masjidILKUpdatePrayerTimes.apply(masjid)));
        if (!isVerified) res.add(new InlineKeyboardRow(masjidILKVerifyMasjidButon.apply(masjid)));
        return res;
    };

    public static ReplyKeyboardMarkup getLocationKB() {
        return replyKeyboardMultiRow.apply(
                List.of(
                        locationButton.andThen(buttonRow).apply(AdminCommand.UNVERIFIED_MASAJID),
                        button.andThen(buttonRow).apply(AdminCommand.BACK)
                )
        );
    }


    static Function<List<KeyboardRow>, ReplyKeyboardMarkup> replyKeyboardMultiRow =
            rows -> ReplyKeyboardMarkup.builder()
                    .oneTimeKeyboard(true)
                    .resizeKeyboard(true)
                    .keyboard(rows)
                    .build();

    static Function<KeyboardRow, ReplyKeyboardMarkup> replyKeyboard =
            row -> ReplyKeyboardMarkup.builder()
                    .oneTimeKeyboard(true)
                    .resizeKeyboard(true)
                    .keyboardRow(row)
                    .build();

    static Function<KeyboardButton, KeyboardRow> buttonRow = KeyboardRow::new;

    static Function<List<KeyboardButton>, KeyboardRow> multiButtonRow = KeyboardRow::new;


    static Function<AdminCommand, KeyboardButton> button =
            command -> KeyboardButton.builder()
                    .text(command.getText())
                    .build();

    static Function<AdminCommand, KeyboardButton> locationButton =
            command -> KeyboardButton.builder()
                    .text(command.getText())
                    .requestLocation(true)
                    .build();

    static Function<AdminCommand, KeyboardButton> contactButton =
            command -> KeyboardButton.builder()
                    .text(command.getText())
                    .requestContact(true)
                    .build();
}
