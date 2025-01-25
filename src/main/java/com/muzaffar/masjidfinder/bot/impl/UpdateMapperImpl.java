package com.muzaffar.masjidfinder.bot.impl;

import com.muzaffar.masjidfinder.bot.UpdateHandler;
import com.muzaffar.masjidfinder.bot.UpdateMapper;
import com.muzaffar.masjidfinder.bot.enums.CallbackCommand;
import com.muzaffar.masjidfinder.bot.enums.Command;
import com.muzaffar.masjidfinder.service.cache.CacheService;
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
    private final CacheService cacheService;


    @Override
    public List<PartialBotApiMethod<?>> map(Update update) {

        List<PartialBotApiMethod<?>> returnList = new ArrayList<>();
        SendMessage sendMessage;

        try {
            if (isMessage(update)) {
                final var command = messageCommand(update) != null ? messageCommand(update) : "";

                if (Objects.equals(command, Command.START.getText())) {
                    sendMessage =  updateHandler.start(update, command);
                    returnList.add(sendMessage);
                    return returnList;
                }

                if (Objects.equals(command, Command.CLOSEST_MASJID.getText())) {
                    sendMessage = updateHandler.closestMasjid(update, command);
                    returnList.add(sendMessage);
                    return returnList;
                }

                if (Objects.equals(command, Command.PRAYER_TIMES.getText())) {
                    //TODO either send location or do search
                    sendMessage = updateHandler.temporaryUnavailable(update);
                    returnList.add(sendMessage);
                    return returnList;
                }

                if (Objects.equals(command, Command.COMMUNITY_PRAYER_TIMES.getText())) {
                    //TODO either send location or do search
//                    sendMessage = updateHandler.temporaryUnavailable(update);
                    sendMessage = updateHandler.commPrayerTimes(update, command);
                    returnList.add(sendMessage);
                    return returnList;

                }

                if (Objects.equals(command, Command.SEARCH.getText())) {
                    sendMessage = updateHandler.searchMasjid(update, command);
                    returnList.add(sendMessage);
                    return returnList;
                }

                if (Objects.equals(command, Command.ABOUT.getText())) {
                    sendMessage = updateHandler.about(update, command);
                    returnList.add(sendMessage);
                    return returnList;
                }

                if (Objects.equals(command, Command.FAVORITES.getText())) {
                    var result = updateHandler.favorites(update, command);
                    returnList.addAll(result);
                    return returnList;
                }

                if (Objects.equals(command, Command.BACK.getText())) {
                    sendMessage = updateHandler.mainMenu(update, command);
                    returnList.add(sendMessage);
                    return returnList;
                }

                if (cacheService.isInSearchMode(getChatId(update))) {
                    var result = updateHandler.findMasajidByName(update, command);
                    returnList.addAll(result);
                    return returnList;
                }

                if (hasLocation(update)) {
                    var result = updateHandler.getMasajid(update, command);
                    returnList.addAll(result);
                    return returnList;
                }
            }

            if (isCallbackQuery(update)) {
                final String newCommand = callbackCommand(update) != null ? callbackCommand(update) : "";

                if (Objects.equals(newCommand, CallbackCommand.SELECTED_MJ_LOCATION.getText())) {
                    var sendLocation = updateHandler.sendMasjidLocation(update);
                    returnList.addAll(sendLocation);
                    return returnList;
                }

                if (Objects.equals(newCommand, CallbackCommand.SET_MJ_AS_FAV.getText())) {
                    sendMessage = updateHandler.setMasjidAsFav(update, newCommand);
                    returnList.add(sendMessage);
                    return returnList;
                }

                if (Objects.equals(newCommand, CallbackCommand.REMOVE_FROM_MJ_AS_FAV.getText())) {
                    sendMessage = updateHandler.removeMasjidFromFav(update, newCommand);
                    returnList.add(sendMessage);
                    return returnList;
                }


            }

        } catch (Exception ignore) {

        }

        //if not recognized
        sendMessage = updateHandler.notRecognised(update);
        returnList.add(sendMessage);
        return returnList;
    }

    public static boolean isMessage(Update update) {
        return update.hasMessage();
    }
}

