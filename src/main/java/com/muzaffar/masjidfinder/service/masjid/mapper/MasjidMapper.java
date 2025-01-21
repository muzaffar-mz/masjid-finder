package com.muzaffar.masjidfinder.service.masjid.mapper;

import com.muzaffar.masjidfinder.domain.entity.Masjid;
import com.muzaffar.masjidfinder.service.masjid.model.MasjidDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MasjidMapper {

    MasjidMapper INSTANCE = Mappers.getMapper(MasjidMapper.class);

    MasjidDTO toMasjidDTO(Masjid masjid, Double distance);
}
