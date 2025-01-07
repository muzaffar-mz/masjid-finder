package com.muzaffar.masjidfinder.service.cache;

import java.util.List;

public interface CacheService {


    void saveSentMessagesId(String chatId, List<Integer> messages);

    List<Integer> getMessagesIdByChatId(String chatId);
}
