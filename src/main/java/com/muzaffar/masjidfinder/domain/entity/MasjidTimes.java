package com.muzaffar.masjidfinder.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "masjid_times", schema = "public")
public class MasjidTimes extends BaseEntity {

    @Column(name = "masjid_id")
    private Long masjidId;

    @Column(name = "fajr")
    private LocalTime fajr;

    @Column(name = "duhr")
    private LocalTime duhr;

    @Column(name = "asr")
    private LocalTime asr;

    @Column(name = "magrib")
    private LocalTime magrib;

    @Column(name = "isha")
    private LocalTime isha;
}
