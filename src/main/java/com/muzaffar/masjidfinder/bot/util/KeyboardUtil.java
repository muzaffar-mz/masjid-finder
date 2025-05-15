package com.muzaffar.masjidfinder.bot.util;

import com.muzaffar.masjidfinder.bot.enums.AdminCommand;
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
import java.util.function.Function;

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

    public static ReplyKeyboardMarkup searchOrSendLocation() {

        var result = locationAndBackRowButtons();
        result.addFirst(searchByNameInRow());

        return ReplyKeyboardMarkup.builder()
                .keyboard(result)
                .oneTimeKeyboard(true)
                .resizeKeyboard(true)
                .build();
    }

    public static ReplyKeyboardMarkup getLocationKB() {
        return ReplyKeyboardMarkup.builder()
                .keyboard(locationAndBackRowButtons())
                .oneTimeKeyboard(true)
                .resizeKeyboard(true)
                .build();
    }

    private static List<KeyboardRow> locationAndBackRowButtons() {

        List<KeyboardRow> result = new ArrayList<>();
        result.add(new KeyboardRow(sendLocationButton()));
        result.add(backButtonInRow());

        return result;
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


    private static KeyboardRow searchByNameInRow() {
        return new KeyboardRow(
                KeyboardButton
                .builder()
                .text(Command.SEARCH.getText())
                .build()
        );
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

    static Function<AdminCommand, KeyboardButton> getButtonFun =
    command -> KeyboardButton.builder()
            .text(command.getText())
            .build();

    //TODO below here is everything chang

    public static InlineKeyboardMarkup getMasjidKeyboardV3(MasjidDTO masjid, Boolean isFavorite) {

        return InlineKeyboardMarkup
                .builder()
                .keyboard(getInlineKeyboardRowsForMasjidV2(masjid, isFavorite))
                .build();
    }

    private static List<InlineKeyboardRow> getInlineKeyboardRowsForMasjidV2(MasjidDTO dto, Boolean isFavorite) {

        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(new InlineKeyboardRow(getInlineKeyboardButtonMasjid(dto)));

        var row = new InlineKeyboardRow();

        if (isFavorite) {
            row.add(getInlineKeyboardButtonRemoveMasjidFromFav(dto));
        } else {
            row.add(getInlineKeyboardButtonSetMasjidAsFav(dto));
        }
        rows.add(row);

        return rows;
    }

    private static InlineKeyboardButton getInlineKeyboardButtonMasjid(MasjidDTO dto) {
        var text = dto.distance() == null ?
                CallbackCommand.SELECTED_MJ_LOCATION.getFullText()
                        .replace("{km}", "nomalum") :
                CallbackCommand.SELECTED_MJ_LOCATION.getFullText()
                        .replace("{km}", dto.distance().toString());

        return InlineKeyboardButton
                .builder()
                .text(text)
                .callbackData(CallbackCommand.SELECTED_MJ_LOCATION.getText() + "_" + dto.id())
                .build();
    }

    private static InlineKeyboardButton getInlineKeyboardButtonSetMasjidAsFav(MasjidDTO dto) {
        return InlineKeyboardButton
                .builder()
                .text(
                        CallbackCommand.SET_MJ_AS_FAV.getFullText()
                )
                .callbackData(CallbackCommand.SET_MJ_AS_FAV.getText() + "_" + dto.id())
                .build();
    }

    private static InlineKeyboardButton getInlineKeyboardButtonRemoveMasjidFromFav(MasjidDTO dto) {
        return InlineKeyboardButton
                .builder()
                .text(
                        CallbackCommand.REMOVE_FROM_MJ_AS_FAV.getFullText()
                )
                .callbackData(CallbackCommand.REMOVE_FROM_MJ_AS_FAV.getText() + "_" + dto.id())
                .build();
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

    public static ReplyKeyboardMarkup defaultSuperAdminKeyboard() {
        var replyKeyboardMarkup = ReplyKeyboardMarkup
                .builder()
                .oneTimeKeyboard(true)
                .resizeKeyboard(true)
                .build();

        List<KeyboardRow> rows = getSuperAdminDefaultKeyboardRows();

        replyKeyboardMarkup.setKeyboard(rows);

        return replyKeyboardMarkup;
    }

    private static @NotNull List<KeyboardRow> getSuperAdminDefaultKeyboardRows() {
        var rowOne = new KeyboardRow();
        var rowTwo = new KeyboardRow();
        var rowThree = new KeyboardRow();

        rowOne.add(getButtonFun.apply(AdminCommand.UNVERIFIED_MASAJID));
        rowTwo.add(getButtonFun.apply(AdminCommand.SEARCH));
        rowThree.add(getButtonFun.apply(AdminCommand.ABOUT));
        List<KeyboardRow> rows = new ArrayList<>();
        rows.add(rowOne);
        rows.add(rowTwo);
        rows.add(rowThree);
        return rows;
    }

    public static InlineKeyboardMarkup getSuperAdminMasjidKeyboard(MasjidDTO dto) {
        return InlineKeyboardMarkup
                .builder()
                .keyboard(getInlineKeyboardRowsForSuperAdmin(dto))
                .build();
    }

    private static List<InlineKeyboardRow> getInlineKeyboardRowsForSuperAdmin(MasjidDTO dto) {
        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(new InlineKeyboardRow(getInlineKeyboardButtonMasjid(dto)));
        return rows;
    }
}
