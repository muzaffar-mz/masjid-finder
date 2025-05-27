package com.muzaffar.masjidfinder.service.masjid;

import com.muzaffar.masjidfinder.bot.model.TgUserDTO;
import com.muzaffar.masjidfinder.model.LocationDTO;
import com.muzaffar.masjidfinder.service.masjid.model.MasjidDTO;
import org.springframework.data.util.Pair;

import java.time.LocalTime;
import java.util.List;

public interface MasjidService {
    List<MasjidDTO> getMasajidClosestToLocation(LocationDTO dto);

    MasjidDTO getMasjid(Long id);

    List<MasjidDTO> getFavsByUserId(Long userId);

    MasjidDTO getDefaultMasjidByUserId(Long userId);

    MasjidDTO setMasjidAsFav(Long id, Long masjidId);

    List<MasjidDTO> findMasajidByName(String name);

    List<MasjidDTO> findUVMasajidByName(String name);

    MasjidDTO removeMasjidFromFav(Long userId, Long masjidId);

    List<MasjidDTO> getUnverifiedMasajidClosestToLocation(LocationDTO locationDTO);

    Pair<List<MasjidDTO>, Long> getFirst15UnverifiedMasajid();

    void updateMasjidName(TgUserDTO user, Long masjidId, String masjidName);

    MasjidDTO verifyMasjidById(TgUserDTO user, Long masjidId);

    MasjidDTO updateMasjidPrayerTimes(TgUserDTO userDTO, Long masjidId, LocalTime bomdod, LocalTime peshin, LocalTime asr, LocalTime shom, LocalTime hufton);
}
