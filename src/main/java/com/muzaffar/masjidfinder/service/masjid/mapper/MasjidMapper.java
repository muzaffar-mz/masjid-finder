package com.muzaffar.masjidfinder.service.masjid.mapper;

import com.muzaffar.masjidfinder.domain.entity.Masjid;
import com.muzaffar.masjidfinder.service.masjid.model.MasjidDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MasjidMapper {

    MasjidDTO toMasjidDTO(Masjid masjid, Double distance);
    MasjidDTO toMasjidDTO(Masjid masjid);
}
