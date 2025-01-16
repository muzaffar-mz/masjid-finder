package com.muzaffar.masjidfinder.domain.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_masjid", schema = "public")
public class UserMasjid extends BaseEntity {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "masjid_id")
    private Long masjidId;

    @Column(name = "is_default")
    private Boolean isDefault = false;
}
