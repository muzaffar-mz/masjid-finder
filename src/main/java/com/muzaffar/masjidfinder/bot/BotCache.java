package com.muzaffar.masjidfinder.bot;

import com.muzaffar.masjidfinder.service.masjid.model.MasjidDTO;

import java.util.List;

public interface BotCache {

    void saveSentMasjids(String chatId, List<MasjidDTO> masjids);
    List<MasjidDTO> getSentMasjids(String chatId);
}
