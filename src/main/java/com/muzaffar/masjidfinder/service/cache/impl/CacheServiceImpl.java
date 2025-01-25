package com.muzaffar.masjidfinder.service.cache.impl;

import com.muzaffar.masjidfinder.service.cache.CacheService;
import com.muzaffar.masjidfinder.service.cache.model.SearchModeDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CacheServiceImpl implements CacheService {

    private Map<String, List<Integer>> deleteMessages;
    private Map<String, SearchModeDTO> searchMode;

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

    @PostConstruct
    public void init() {
        this.deleteMessages = new ConcurrentHashMap<>();
        this.searchMode = new ConcurrentHashMap<>();
    }


}
