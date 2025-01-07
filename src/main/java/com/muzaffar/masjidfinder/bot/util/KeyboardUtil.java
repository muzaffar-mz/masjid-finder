package com.muzaffar.masjidfinder.bot.util;

import com.muzaffar.masjidfinder.bot.enums.CallbackCommand;
import com.muzaffar.masjidfinder.service.masjid.model.MasjidDTO;
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

        //"Yaqin Masjidlar"
        //"Namoz Vaqtlari"
        //“Iqoma Vaqtlari”
        //“Mening Masjidlarim”
        //"Bot haqida"

        //var row = new KeyboardRow();
        //
        //        var message = "Joylashuvni yuborish";
        //        var kb = KeyboardButton
        //                .builder()
        //                .text(message)
        //                .requestLocation(true)
        //                .build();
        //
        //        row.add(kb);
        //
        //
        //        List<KeyboardRow> listOfKeyboardRows = new ArrayList<>();
        //        listOfKeyboardRows.add(row);
        //
        //        var replyKeyboardMarkup = ReplyKeyboardMarkup
        //                .builder()
        //                        .keyboard(listOfKeyboardRows)
        //                                .oneTimeKeyboard(true)
        //                                        .resizeKeyboard(true)
        //                .build();
        //        return replyKeyboardMarkup;
    }

    //TODO below here is everything chang

    public static ReplyKeyboardMarkup getLocationKB() {
        var row = new KeyboardRow();

        var message = "Joylashuvni yuborish";
        var kb = KeyboardButton
                .builder()
                .text(message)
                .requestLocation(true)
                .build();

        row.add(kb);


        List<KeyboardRow> listOfKeyboardRows = new ArrayList<>();
        listOfKeyboardRows.add(row);

        var replyKeyboardMarkup = ReplyKeyboardMarkup
                .builder()
                        .keyboard(listOfKeyboardRows)
                                .oneTimeKeyboard(true)
                                        .resizeKeyboard(true)
                .build();
        return replyKeyboardMarkup;
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
                        .text(dto.name() + " " + dto.distance() + " KM uzoqda")
                        .callbackData(CallbackCommand.SELECTED_MJ_LOCATION.getText() + "_" + dto.id())
                        .build()
        );
    }
}
