package com.muzaffar.masjidfinder.bot.impl;

import com.muzaffar.masjidfinder.bot.AdminUpdateHandler;
import com.muzaffar.masjidfinder.bot.UpdateHandler;
import com.muzaffar.masjidfinder.bot.UpdateMapper;
import com.muzaffar.masjidfinder.bot.enums.AdminCallbackCommand;
import com.muzaffar.masjidfinder.bot.enums.AdminCommand;
import com.muzaffar.masjidfinder.bot.enums.CallbackCommand;
import com.muzaffar.masjidfinder.bot.enums.Command;
import com.muzaffar.masjidfinder.service.cache.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static com.muzaffar.masjidfinder.bot.util.UpdateUtil.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateMapperImpl implements UpdateMapper {

    private final UpdateHandler updateHandler;
    private final AdminUpdateHandler adminUpdateHandler;
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

    @Override
    public List<PartialBotApiMethod<?>> adminMap(Update update) {
        List<PartialBotApiMethod<?>> returnList = new ArrayList<>();
        SendMessage sendMessage;

        try {
            if (isMessage.test(update)) {
                final var command = messageCommand(update) != null ? messageCommand(update) : "";

                if (Objects.equals(command, AdminCommand.START.getText())) {
                    sendMessage =  adminUpdateHandler.start(update, command);
                    returnList.add(sendMessage);
                    return returnList;
                }

                if (hasContact.test(update.getMessage())) {
                    sendMessage = adminUpdateHandler.registerAdmin(update);
                    returnList.add(sendMessage);
                    return returnList;
                }

                if (!adminUpdateHandler.isUserAuthorized(update)) {
                    sendMessage = adminUpdateHandler.unauthorizedUser(update);
                    returnList.add(sendMessage);
                    return returnList;
                }

                if (Objects.equals(command, AdminCommand.UNVERIFIED_MASAJID.getText())) {
                    sendMessage = adminUpdateHandler.unverifiedMasajidSection(update, command);
                    returnList.add(sendMessage);
                    return returnList;
                }

                if (Objects.equals(command, AdminCommand.TOTAL_UV_LIST.getText())) {
                    var result = adminUpdateHandler.getAllUnverifiedMasajid(update, command);
                    returnList.addAll(result);
                    return returnList;
                }

                if (Objects.equals(command, AdminCommand.BACK.getText())) {
                    sendMessage = adminUpdateHandler.mainMenu(update, command);
                    returnList.add(sendMessage);
                    return returnList;
                }

                if (Objects.equals(command, AdminCommand.SEARCH_UV.getText())) {
                    sendMessage = adminUpdateHandler.searchMasjidButton(update, command, true);
                    returnList.add(sendMessage);
                    return returnList;
                }

                if (Objects.equals(command, AdminCommand.GET_NEAR_5_UV_MASAJID.getText())) {
                    sendMessage = adminUpdateHandler.preGetFiveNearMasajid(update, command, true);
                    returnList.add(sendMessage);
                    return returnList;
                }

                if (cacheService.isAdminInSearchMode(getChatId(update))) {
                    var result = adminUpdateHandler.findMasjidByName(update, command);
                    returnList.addAll(result);
                    return returnList;
                }

                if (cacheService.isAdminInUpdateMasjidMode(getChatId(update))) {
                    sendMessage = adminUpdateHandler.updateMasjidName(update, command);
                    returnList.add(sendMessage);
                    return returnList;
                }

                if (cacheService.isAdminInUpdatePrayerTimesMode(getChatId(update))) {
                    sendMessage = adminUpdateHandler.updateMasjidPrayerTimes(update, command);
                    returnList.add(sendMessage);
                    return returnList;
                }

                if (cacheService.isAdminInSearchByIdMode(getChatId(update))) {
                    sendMessage = adminUpdateHandler.getMasjidById(update, command);
                    returnList.add(sendMessage);
                    return returnList;
                }

                if (Objects.equals(command, AdminCommand.UPDATE_COM_PRAY_TIME.getText())) {
                    sendMessage = adminUpdateHandler.updateComPrayTime(update, command);
                    returnList.add(sendMessage);
                    return returnList;
                }

                if (Objects.equals(command, AdminCommand.GET_ASSIGNED_MASJID.getText())) {
                    var result = adminUpdateHandler.getAssignedMasajid(update);
                    returnList.addAll(result);
                    return returnList;
                }

                if (Objects.equals(command, AdminCommand.SEARCH.getText())) {
                    sendMessage = adminUpdateHandler.searchMasjidButton(update, command, false);
                    returnList.add(sendMessage);
                    return returnList;
                }

                if (Objects.equals(command, AdminCommand.SEARCH_BY_ID.getText())) {
                    sendMessage = adminUpdateHandler.preSearchById(update, command);
                    returnList.add(sendMessage);
                    return returnList;
                }

                if (Objects.equals(command, AdminCommand.GET_NEAR_5_MASAJID.getText())) {
                    sendMessage = adminUpdateHandler.preGetFiveNearMasajid(update, command, false);
                    returnList.add(sendMessage);
                    return returnList;
                }


                if (hasLocation(update)) {
                    if (cacheService.isAdminToGetNearMasajidMode(getChatId(update))) {
                        var result = adminUpdateHandler.getNearFiveMasajid(update);
                        returnList.addAll(result);
                        return returnList;
                    }
                }

                if (Objects.equals(command, AdminCommand.ABOUT.getText())) {
                    sendMessage = adminUpdateHandler.about(update, command);
                    returnList.add(sendMessage);
                    return returnList;
                }
            }

            if (!adminUpdateHandler.isUserAuthorized(update)) {
                sendMessage = adminUpdateHandler.unauthorizedUser(update);
                returnList.add(sendMessage);
                return returnList;
            }

            if (isCallbackQuery(update)) {
                //TODO DO THE ADMIN SECURITY CHECK
                final String newCommand = callbackCommand(update) != null ? callbackCommand(update) : "";

                //TODO rename UV_MASJID make it more universal
                if (Objects.equals(newCommand, AdminCallbackCommand.UV_MASJID.getText())) {
                    sendMessage = adminUpdateHandler.getMasjidUpdateService(update);
                    returnList.add(sendMessage);
                    return returnList;
                }

                if (Objects.equals(newCommand, AdminCallbackCommand.UPDATE_MASJID_NAME.getText())) {
                    sendMessage = adminUpdateHandler.preUpdateMasjidName(update);
                    returnList.add(sendMessage);
                    return returnList;
                }

                if (Objects.equals(newCommand, AdminCallbackCommand.UPDATE_MASJID_PRAYER_TIME.getText())) {
                    sendMessage = adminUpdateHandler.preUpdatePrayerTimes(update);
                    returnList.add(sendMessage);
                    return returnList;
                }

                if (Objects.equals(newCommand, AdminCallbackCommand.VERIFY_MASJID.getText())) {
                    sendMessage = adminUpdateHandler.verifyMasjid(update);
                    returnList.add(sendMessage);
                    return returnList;
                }

                if (Objects.equals(newCommand, AdminCallbackCommand.BACK.getText())) {
                    sendMessage = adminUpdateHandler.mainMenu(update, newCommand);
                    returnList.add(sendMessage);
                    return returnList;
                }
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }

        if (!adminUpdateHandler.isUserAuthorized(update)) {
            sendMessage = adminUpdateHandler.unauthorizedUser(update);
            returnList.add(sendMessage);
            return returnList;
        }

        //if not recognized
        sendMessage = adminUpdateHandler.notRecognised(update);
        returnList.add(sendMessage);
        return returnList;
    }

    public static boolean isMessage(Update update) {
        return update.hasMessage();
    }

    static Predicate<Update> isMessage = Update::hasMessage;

    static Predicate<Message> hasContact = Message::hasContact;

}

