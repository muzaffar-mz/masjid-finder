package com.muzaffar.masjidfinder.domain.entity;


import com.muzaffar.masjidfinder.domain.entity.enums.UserMasjidType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_masjid", schema = "public")
@EntityListeners(AuditingEntityListener.class)
public class UserMasjid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "masjid_id", nullable = false)
    private Long masjidId;

    @Column(name = "is_default")
    private Boolean isDefault = false;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserMasjidType type;

    @CreatedDate
    @Column(name = "date_created", nullable = false, updatable = false)
    private LocalDateTime dateCreated;
}
