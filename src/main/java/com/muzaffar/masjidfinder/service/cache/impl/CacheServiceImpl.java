package com.muzaffar.masjidfinder.service.cache.impl;

import com.muzaffar.masjidfinder.service.cache.CacheService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CacheServiceImpl implements CacheService {

    private Map<String, List<Integer>> deleteMessages;


    @Override
    public void saveSentMessagesId(String chatId, List<Integer> messages) {
        deleteMessages.computeIfAbsent(chatId, _ -> new ArrayList<>()).addAll(messages);
    }

    @Override
    public List<Integer> getMessagesIdByChatId(String chatId) {
        return deleteMessages.remove(chatId);
    }


    @PostConstruct
    public void init() {
        this.deleteMessages = new ConcurrentHashMap<>();
    }


}
