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
@Table(name = "meta_data", schema = "public")
public class MetaData extends BaseEntity {

    @Column(name = "key", nullable = false)
    private String key;
    @Column(name = "value", nullable = false)
    private String value;

}
