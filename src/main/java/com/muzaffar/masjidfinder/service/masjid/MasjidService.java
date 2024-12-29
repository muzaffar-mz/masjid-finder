package com.muzaffar.masjidfinder.service.masjid;

import com.muzaffar.masjidfinder.model.LocationDTO;
import com.muzaffar.masjidfinder.service.masjid.model.MasjidDTO;

import java.util.List;

public interface MasjidService {
    List<MasjidDTO> getMasjidsClosestToLocation(LocationDTO dto);

    MasjidDTO getMasjid(Long id);
}
