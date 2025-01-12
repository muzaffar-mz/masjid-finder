package com.muzaffar.masjidfinder.bot.util;

import com.muzaffar.masjidfinder.bot.enums.CallbackCommand;
import com.muzaffar.masjidfinder.service.masjid.model.MasjidDTO;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class KeyboardUtil {

    public static ReplyKeyboardMarkup defaultKeyboard() {
        var replyKeyboardMarkup = ReplyKeyboardMarkup
                .builder()
                .oneTimeKeyboard(true)
                .resizeKeyboard(true)
                .build();

        List<KeyboardRow> rows = getDefaultKeyboardRows();

        replyKeyboardMarkup.setKeyboard(rows);

        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup getLocationKB() {

        List<KeyboardRow> listOfKeyboardRows = new ArrayList<>();

        listOfKeyboardRows.add(new KeyboardRow(sendLocationButton()));
        listOfKeyboardRows.add(backButtonInRow());

        return ReplyKeyboardMarkup.builder()
                .keyboard(listOfKeyboardRows)
                .oneTimeKeyboard(true)
                .resizeKeyboard(true)
                .build();
    }

    private static @NotNull List<KeyboardRow> getDefaultKeyboardRows() {
        var rowOne = new KeyboardRow();
        var rowTwo = new KeyboardRow();
        var rowThree = new KeyboardRow();

        rowOne.add(closeMasajidButton());
        rowOne.add(prayerTimesButton());

        rowTwo.add(communityPrayerTimesButton());
        rowTwo.add(favoriteMasajid());

        rowThree.add(aboutBotButton());


        List<KeyboardRow> rows = new ArrayList<>();
        rows.add(rowOne);
        rows.add(rowTwo);
        rows.add(rowThree);
        return rows;
    }

    public static KeyboardRow backButtonInRow() {
        return new KeyboardRow(backButton());
    }

    public static KeyboardButton favoriteMasajid() {
        return KeyboardButton
                .builder()
                .text("💚 Mening Masjidlarim")
                .build();
    }

    public static KeyboardButton communityPrayerTimesButton() {
        return KeyboardButton
                .builder()
                .text("⏰ Jamoat Vaqtlari")
                .build();
    }

    public static KeyboardButton prayerTimesButton() {
        return KeyboardButton
                .builder()
                .text("🕐 Namoz Vaqtlari")
                .build();
    }

    public static KeyboardButton closeMasajidButton() {
        return KeyboardButton
                .builder()
                .text("🕌 Yaqin Masjidlar")
                .build();
    }

    public static KeyboardButton aboutBotButton() {
        return KeyboardButton
                .builder()
                .text("ℹ️ Bot Haqida")
                .build();
    }

    public static KeyboardButton backButton() {
        return KeyboardButton
                .builder()
                .text("⬅️ Ortga qaytish")
                .build();
    }

    public static KeyboardButton sendLocationButton() {
        return KeyboardButton
                .builder()
                .text("📍 Eng yaqin masjidlarni ko'rish")
                .requestLocation(true)
                .build();
    }

    //TODO below here is everything chang


    public static InlineKeyboardMarkup getMasjidKeyboard(MasjidDTO masjid) {
        return InlineKeyboardMarkup
                .builder()
                .keyboardRow(getInlineKeyboardRowForMasjid(masjid))
                .build();
    }

    private static InlineKeyboardRow getInlineKeyboardRowForMasjid(MasjidDTO dto) {
        return new InlineKeyboardRow(
                InlineKeyboardButton
                        .builder()
                        .text(
                                CallbackCommand.SELECTED_MJ_LOCATION.getFullText()
                                        .replace("{m_name}", dto.name())
                                        .replace("{km}", dto.distance().toString())
                        )
                        .callbackData(CallbackCommand.SELECTED_MJ_LOCATION.getText() + "_" + dto.id())
                        .build()
        );
    }

    public static InlineKeyboardMarkup getInlineMainMenuButton() {
        return InlineKeyboardMarkup
                .builder()
                .keyboardRow(getInlineKeyboardRowForMainMenu())
                .build();
    }

    public static InlineKeyboardRow getInlineKeyboardRowForMainMenu() {
        return new InlineKeyboardRow(
                InlineKeyboardButton
                        .builder()
                        .text(CallbackCommand.BACK_TO_MAIN_MENU.getFullText())
                        .callbackData(CallbackCommand.BACK_TO_MAIN_MENU.getText())
                        .build()
        );
    }
}
