package com.muzaffar.masjidfinder.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.muzaffar.masjidfinder.domain.entity.enums.MasjidNamesGroup;
import com.muzaffar.masjidfinder.domain.entity.enums.MasjidStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "masjid", schema = "public")
public class Masjid extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "name_group")
    @Enumerated(EnumType.ORDINAL)
    private MasjidNamesGroup namesGroup;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private MasjidStatus status;

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


    @OneToMany(mappedBy = "defaultMasjid", fetch = FetchType.LAZY)
    @JsonManagedReference
    List<User> users;

    @Column(name = "notes")
    private String notes;
}
