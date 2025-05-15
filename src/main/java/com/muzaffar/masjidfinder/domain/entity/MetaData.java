package com.muzaffar.masjidfinder.domain.entity;


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

    private String key;
    private String value;

}
