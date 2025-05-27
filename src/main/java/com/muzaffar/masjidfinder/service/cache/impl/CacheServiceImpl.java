package com.muzaffar.masjidfinder.service.cache.impl;

import com.muzaffar.masjidfinder.bot.model.TgUserDTO;
import com.muzaffar.masjidfinder.service.cache.CacheService;
import com.muzaffar.masjidfinder.service.cache.model.AdminMode;
import com.muzaffar.masjidfinder.service.cache.model.AdminModesDTO;
import com.muzaffar.masjidfinder.service.cache.model.SearchModeDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CacheServiceImpl implements CacheService {

    private Map<String, List<Integer>> deleteMessages;
    private Map<String, SearchModeDTO> searchMode;
    private Map<String, AdminModesDTO> adminModes;

    @Value("${telegram.max-attempts}")
    private Integer MAX_ATTEMPTS;

    @Override
    public void saveSentMessagesId(String chatId, List<Integer> messages) {
        deleteMessages.computeIfAbsent(chatId, k -> new ArrayList<>()).addAll(messages);
    }

    @Override
    public List<Integer> getMessagesIdByChatId(String chatId) {
        return deleteMessages.remove(chatId);
    }

    @Override
    public Boolean isSearchModeAllowed(String chatId) {
        var cached = searchMode.compute(chatId, (key, cache) -> {
            if (cache == null) {
                return new SearchModeDTO(chatId);
            }

            if (cache.getRestartTime().isBefore(LocalDateTime.now())) {
                //if restart time already passed -> we allow user to enter search mode by restarting its attempts number and restart time
                cache.restart();
            } else if (cache.getAttempts() >= MAX_ATTEMPTS) {
                //if attempts already 5 but restart time is not expired
                cache.turnOffSearchMode();
            }

            return cache;
        });

        return cached.isSearchModeOn();
    }

    @Override
    public Boolean isInSearchMode(String chatId) {
        var cached = searchMode.compute(chatId, (key, value) -> {
            if (Objects.nonNull(value) && value.isSearchModeOn() ) {
                value.incrementAttempts();

                if (value.getAttempts()> MAX_ATTEMPTS) {
                    value.turnOffSearchMode();
                }
            }
            return value;
        });
        return Objects.nonNull(cached) && cached.isSearchModeOn();
    }

    @Override
    public void adminToSearchMode(String chatId) {
        adminModes.compute(chatId, (key, cache) -> {
            if (cache == null) {
                return new AdminModesDTO(key, AdminMode.UV_SEARCH, null);
            }
            cache.setAdminModes(AdminMode.UV_SEARCH);
            return cache;
        });
    }

    @Override
    public void adminToGeneralMode(String chatId) {
        adminModes.remove(chatId);
    }

    @Override
    public boolean isAdminInSearchMode(String chatId) {
        var cached = adminModes.get(chatId);
        return Objects.nonNull(cached) && Objects.equals(AdminMode.UV_SEARCH, cached.getAdminModes());
    }

    @Override
    public void adminToUpdateMasjidNameMode(String chatId, Long masjidId) {
        adminModes.compute(chatId, (key, cache) -> {
            if (cache == null) {
                return new AdminModesDTO(key, AdminMode.MASJID_UPDATE, masjidId);
            }
            cache.setAdminModes(AdminMode.MASJID_UPDATE);
            cache.setMasjidId(masjidId);
            return cache;
        });
    }

    @Override
    public boolean isAdminInUpdateMasjidMode(String chatId) {
        var cached = adminModes.get(chatId);
        return Objects.nonNull(cached) && Objects.equals(AdminMode.MASJID_UPDATE, cached.getAdminModes());
    }

    @Override
    public Long getMasjidIdToUpdate(String chatId) {
        var cached = adminModes.remove(chatId);
        return cached == null ? null : cached.getMasjidId();
    }

    @Override
    public void adminToUpdatePrayerTimesMode(String chatId, Long masjidId) {
        adminModes.compute(chatId, (key, cache) -> {
            if (cache == null) {
                return new AdminModesDTO(key, AdminMode.PRAYER_TIME_UPDATE, masjidId);
            }
            cache.setAdminModes(AdminMode.PRAYER_TIME_UPDATE);
            cache.setMasjidId(masjidId);
            return cache;
        });
    }

    @Override
    public boolean isAdminInUpdatePrayerTimesMode(String chatId) {
        var cached = adminModes.get(chatId);
        return Objects.nonNull(cached) && Objects.equals(AdminMode.PRAYER_TIME_UPDATE, cached.getAdminModes());
    }

    @PostConstruct
    public void init() {
        this.deleteMessages = new ConcurrentHashMap<>();
        this.searchMode = new ConcurrentHashMap<>();
        this.adminModes = new ConcurrentHashMap<>();
    }


}
