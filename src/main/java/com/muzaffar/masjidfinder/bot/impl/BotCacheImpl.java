package com.muzaffar.masjidfinder.bot.impl;

import com.muzaffar.masjidfinder.bot.BotCache;
import com.muzaffar.masjidfinder.service.masjid.model.MasjidDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BotCacheImpl implements BotCache {

    private final Map<String, List<Integer>> cache = new HashMap<>();
    private final Map<String, List<MasjidDTO>> masjidCache = new HashMap<>();

    @Override
    public void saveSentMasjids(String chatId, List<MasjidDTO> masjids) {
        masjidCache.put(chatId, masjids);
    }

    @Override
    public List<MasjidDTO> getSentMasjids(String chatId) {
        return List.of();
    }
}
