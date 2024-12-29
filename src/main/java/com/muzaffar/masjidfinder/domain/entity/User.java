package com.muzaffar.masjidfinder.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.muzaffar.masjidfinder.domain.entity.enums.InterfaceLanguage;
import com.muzaffar.masjidfinder.domain.entity.enums.UserRole;
import com.muzaffar.masjidfinder.domain.entity.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user", schema = "public")
public class User extends BaseEntity {

    @Column(name = "phone", unique = true)
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "telegram_id")
    private Long telegramId;

    @Column(name = "language")
    @Enumerated(EnumType.ORDINAL)
    private InterfaceLanguage language;

    @Column(name = "password")
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "masjid_id")
    @JsonBackReference
    private Masjid defaultMasjid;

    @Column(name = "notification")
    private Boolean notification = false;
}
