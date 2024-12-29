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

    public static InlineKeyboardMarkup getMasjidsKeyboard(List<MasjidDTO> list) {

        var kb = InlineKeyboardMarkup
                .builder()
                .build();

        List<InlineKeyboardRow> rows = new ArrayList<>();

        for (MasjidDTO dto : list) {
            rows.add(getInlineKeyboardRowForMasjid(dto));
        }

        kb.setKeyboard(rows);
        return kb;

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


//    private static List<InlineKeyboardRow> getInlineKeyboardRowForMasjid(MasjidDTO dto) {
//
//        //InlineKeyboardRow keyboardRow = new InlineKeyboardRow(
//        //                InlineKeyboardButton
//        //                        .builder()
//        //                        .text(list.getFirst().name() + " " + list.getFirst().distance() + " KM away")
//        //                        .callbackData(CallbackCommand.SELECTED_MJ_INFORMATION.getText() + "_" + list.getFirst().id())
//        //                        .build()
//        //                //allbackCommand.SELECTED_MJ_INFORMATION.getText() + "_" + dto.id()
//        //        );
//        //
//        //        InlineKeyboardRow keyboardRow1 = new InlineKeyboardRow(
//        //                InlineKeyboardButton
//        //                        .builder()
//        //                        .text("\uD83D\uDCCD Location")
//        //                        .callbackData("TEST_TEST")
//        //                        .build()
//        //                //allbackCommand.SELECTED_MJ_INFORMATION.getText() + "_" + dto.id()
//        //        );
//        //
//        //        List<InlineKeyboardRow> lists = new ArrayList<>();
//        //        lists.add(keyboardRow);
//        //        lists.add(keyboardRow1);
//        var infoButton = InlineKeyboardButton.builder().build();
//        infoButton.setText(dto.name() + " " + dto.distance() + " KM away");
//        infoButton.setCallbackData(CallbackCommand.SELECTED_MJ_INFORMATION.getText() + "_" + dto.id());
//
//        var ikr = new InlineKeyboardRow();
//        ikr.add(infoButton);
//
//        var locationButton = InlineKeyboardButton.builder().build();
//        locationButton.setText("\uD83D\uDCCD Location");
//        locationButton.setCallbackData(CallbackCommand.SELECTED_MJ_LOCATION.getText() + "_" + dto.id());
//
//        var prayerTimeButton = InlineKeyboardButton.builder().build();
//        prayerTimeButton.setText("\uD83D\uDD50\uD83E\uDDCE\uD83C\uDFFB\u200D♂\uFE0F Prayer time");
//        prayerTimeButton.setCallbackData(CallbackCommand.SELECTED_MJ_PRAYER_TIMES.getText() + "_" + dto.id());
//
//        var ikr2 = new InlineKeyboardRow();
//        ikr2.add(locationButton);
//        ikr2.add(prayerTimeButton);
//
//        List<InlineKeyboardRow> listOfRows = new ArrayList<>();
//        listOfRows.add(ikr);
//        listOfRows.add(ikr2);
//        return listOfRows;
//    }
}
