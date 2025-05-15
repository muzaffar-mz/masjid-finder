package com.muzaffar.masjidfinder.service.masjid.model;

import com.muzaffar.masjidfinder.domain.entity.Masjid;
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
    public MasjidDTO(Masjid masjid) {
        this(
                masjid.getId(),
                masjid.getName(),
                masjid.getAddress(),
                masjid.getLatitude(),
                masjid.getLongitude(),
                masjid.getStatus(),
                null,
                masjid.getFajr(),
                masjid.getDuhr(),
                masjid.getAsr(),
                masjid.getMagrib(),
                masjid.getIsha()
        );
    }

    public MasjidDTO(Masjid masjid, Double distance) {
        this(
                masjid.getId(),
                masjid.getName(),
                masjid.getAddress(),
                masjid.getLatitude(),
                masjid.getLongitude(),
                masjid.getStatus(),
                distance,
                masjid.getFajr(),
                masjid.getDuhr(),
                masjid.getAsr(),
                masjid.getMagrib(),
                masjid.getIsha()
        );
    }

    public String getNameAndPrayerTimesForBot() {
        return "*" + this.name + "*" + " \nBomdod: %s, Peshin: %s, Asr: %s, Shom: %s, Xufton: %s"
                .formatted(bomdod, peshin, asr, shom, hufton);
    }

    public String getIdAndName() {
        return "*" + this.id + "\\. " + this.name + "*";
    }
}
