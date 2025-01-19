package com.muzaffar.masjidfinder.bot.util;

import com.muzaffar.masjidfinder.bot.enums.CallbackCommand;
import com.muzaffar.masjidfinder.bot.enums.Command;
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

    private static KeyboardRow backButtonInRow() {
        return new KeyboardRow(backButton());
    }

    private static KeyboardButton favoriteMasajid() {
        return KeyboardButton
                .builder()
                .text(Command.FAVORITES.getText())
                .build();
    }

    private static KeyboardButton communityPrayerTimesButton() {
        return KeyboardButton
                .builder()
                .text(Command.COMMUNITY_PRAYER_TIMES.getText())
                .build();
    }

    private static KeyboardButton prayerTimesButton() {
        return KeyboardButton
                .builder()
                .text(Command.PRAYER_TIMES.getText())
                .build();
    }

    private static KeyboardButton closeMasajidButton() {
        return KeyboardButton
                .builder()
                .text(Command.CLOSEST_MASJID.getText())
                .build();
    }

    private static KeyboardButton aboutBotButton() {
        return KeyboardButton
                .builder()
                .text(Command.ABOUT.getText())
                .build();
    }

    private static KeyboardButton backButton() {
        return KeyboardButton
                .builder()
                .text(Command.BACK.getText())
                .build();
    }

    private static KeyboardButton sendLocationButton() {
        return KeyboardButton
                .builder()
                .text(Command.GET_THE_CLOSEST_MASAJID.getText())
                .requestLocation(true)
                .build();
    }

    //TODO below here is everything chang

    public static InlineKeyboardMarkup getMasjidKeyboardV2(MasjidDTO masjid) {
        return InlineKeyboardMarkup
                .builder()
                .keyboard(getInlineKeyboardRowsForMasjid(masjid))
                .build();
    }

    public static InlineKeyboardMarkup getMasjidKeyboardV3(MasjidDTO masjid) {
        return InlineKeyboardMarkup
                .builder()
                .keyboard(getInlineKeyboardRowsForMasjidV2(masjid))
                .build();
    }

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

    private static List<InlineKeyboardRow> getInlineKeyboardRowsForMasjid(MasjidDTO dto) {
        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(
                new InlineKeyboardRow(
                        InlineKeyboardButton
                                .builder()
                                .text(
                                        "📍 " + dto.distance() + " KM uzoqda. Yo'nalishni olish"
//                                        CallbackCommand.SELECTED_MJ_LOCATION.getFullText()
//                                                .replace("{m_name}", dto.name())
//                                                .replace("{km}", dto.distance().toString())
                                )
                                .callbackData(CallbackCommand.SELECTED_MJ_LOCATION.getText() + "_" + dto.id())
                                .build()
                )
        );

        rows.add(
                new InlineKeyboardRow(
                        InlineKeyboardButton
                                .builder()
                                .text(
                                        CallbackCommand.SET_MJ_AS_FAV.getFullText()
                                )
                                .callbackData(CallbackCommand.SET_MJ_AS_FAV.getText() + "_" + dto.id())
                                .build()
                )
        );

        return rows;
    }

    private static List<InlineKeyboardRow> getInlineKeyboardRowsForMasjidV2(MasjidDTO dto) {

        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(
                new InlineKeyboardRow(
                        InlineKeyboardButton
                                .builder()
                                .text(
                                        CallbackCommand.SELECTED_MJ_LOCATION.getFullText()
                                                .replace("{m_name}", dto.name())
                                                .replace("{km}", "nomalum")
                                )
                                .callbackData(CallbackCommand.SELECTED_MJ_LOCATION.getText() + "_" + dto.id())
                                .build()
                )
        );

        rows.add(
                new InlineKeyboardRow(
                        InlineKeyboardButton
                                .builder()
                                .text(
                                        CallbackCommand.REMOVE_FROM_MJ_AS_FAV.getFullText()
                                )
                                .callbackData(CallbackCommand.REMOVE_FROM_MJ_AS_FAV.getText() + "_" + dto.id())
                                .build()
                )
        );

        return rows;
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

    public static ReplyKeyboardMarkup backKeyboard() {

        List<KeyboardRow> listOfKeyboardRows = new ArrayList<>();
        listOfKeyboardRows.add(backButtonInRow());

        return ReplyKeyboardMarkup.builder()
                .keyboard(listOfKeyboardRows)
                .oneTimeKeyboard(true)
                .resizeKeyboard(true)
                .build();
    }
}
