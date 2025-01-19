package com.muzaffar.masjidfinder.service.masjid;

import com.muzaffar.masjidfinder.model.LocationDTO;
import com.muzaffar.masjidfinder.service.masjid.model.MasjidDTO;

import java.util.List;

public interface MasjidService {
    List<MasjidDTO> getMasajidClosestToLocation(LocationDTO dto);

    MasjidDTO getMasjid(Long id);

    List<MasjidDTO> getFavsByUserId(Long userId);

    MasjidDTO getDefaultMasjidByUserId(Long userId);

    MasjidDTO setMasjidAsFav(Long id, Long masjidId);
}
