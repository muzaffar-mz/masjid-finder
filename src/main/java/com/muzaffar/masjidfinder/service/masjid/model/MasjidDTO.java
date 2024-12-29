package com.muzaffar.masjidfinder.service.masjid.model;

import com.muzaffar.masjidfinder.domain.entity.enums.MasjidStatus;

import java.time.LocalTime;

public record MasjidDTO(
        Long id,
        String name,
        String address,
        Double latitude,
        Double longitude,
        MasjidStatus status,
        Double distance,
        LocalTime bomdod,
        LocalTime peshin,
        LocalTime asr,
        LocalTime shom,
        LocalTime hufton
) {
}
