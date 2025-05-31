package com.muzaffar.masjidfinder.service.cache;

import com.muzaffar.masjidfinder.bot.model.TgUserDTO;
import com.muzaffar.masjidfinder.service.cache.model.AdminModesDTO;

import java.util.List;

public interface CacheService {


    void saveSentMessagesId(String chatId, List<Integer> messages);

    List<Integer> getMessagesIdByChatId(String chatId);

    Boolean isSearchModeAllowed(String chatId);
    Boolean isInSearchMode(String chatId);

    void adminToSearchMode(String chatId, Boolean isUnverified);

    void adminToGeneralMode(String chatId);

    boolean isAdminInSearchMode(String chatId);

    void adminToUpdateMasjidNameMode(String chatId, Long masjidId);

    boolean isAdminInUpdateMasjidMode(String chatId);

    Long getMasjidIdToUpdate(String chatId);

    void adminToUpdatePrayerTimesMode(String chatId, Long masjidId);

    boolean isAdminInUpdatePrayerTimesMode(String chatId);

    void adminToGetNearMasajidMode(String chatId, Boolean isUnverified);

    boolean isAdminToGetNearMasajidMode(String chatId);

    AdminModesDTO getAdminCacheDTO(String chatId);

    void adminToSearchByIdMode(String chatId);

    boolean isAdminInSearchByIdMode(String chatId);

}
