package com.muzaffar.masjidfinder.bot.util;

import com.muzaffar.masjidfinder.bot.enums.AdminCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;
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

    public static ReplyKeyboardMarkup unverifiedMasajidSectonKB() {
        return replyKeyboardMultiRow.apply(
                List.of(
                        button.andThen(buttonRow).apply(AdminCommand.TOTAL_UV_LIST),
                        button.andThen(buttonRow).apply(AdminCommand.SEARCH_UV),
                        button.andThen(buttonRow).apply(AdminCommand.GET_NEAR_5_UV_MASAJID)
                )
        );
    }

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
