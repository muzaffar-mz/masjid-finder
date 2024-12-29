package com.muzaffar.masjidfinder.bot.impl;

import com.muzaffar.masjidfinder.bot.UpdateHandler;
import com.muzaffar.masjidfinder.bot.UpdateMapper;
import com.muzaffar.masjidfinder.bot.enums.CallbackCommand;
import com.muzaffar.masjidfinder.bot.enums.Command;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.muzaffar.masjidfinder.bot.util.UpdateUtil.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateMapperImpl implements UpdateMapper {

    private final UpdateHandler updateHandler;


    @Override
    public List<PartialBotApiMethod<?>> map(Update update) {

        List<PartialBotApiMethod<?>> returnList = new ArrayList<>();
//        List<SendMessage> returnList = new ArrayList<>();
        SendMessage sendMessage = null;

        try {
            if (isMessage(update)) {
                //nc stands for New Command
                final var nc = messageCommand(update) != null ? messageCommand(update) : "";

                if (Objects.equals(nc, Command.START.getText())) {
                    sendMessage =  updateHandler.start(update);
                    returnList.add(sendMessage);
                    return returnList;
                }

                if (hasLocation(update)) {
                    sendMessage = updateHandler.getMasjids(update);
                    returnList.add(sendMessage);
                    return returnList;
                }
            }

            if (isCallbackQuery(update)) {
                final String newCommand = callbackCommand(update) != null ? callbackCommand(update) : "";

                if (Objects.equals(newCommand, CallbackCommand.SELECTED_MJ_LOCATION.getText())) {
                    var deleteMessage = updateHandler.deleteMessage(update);
                    var sendLocation = updateHandler.sendMasjidLocation(update);
                    returnList.add(deleteMessage);
                    returnList.addAll(sendLocation);
                    return returnList;
                }
            }


        } catch (Exception ignore) {

        }

        return null;
    }

    public static boolean isMessage(Update update) {
        return update.hasMessage();
    }
}

