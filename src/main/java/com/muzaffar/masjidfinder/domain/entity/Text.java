package com.muzaffar.masjidfinder.domain.entity;

import com.muzaffar.masjidfinder.bot.enums.Command;
import com.muzaffar.masjidfinder.domain.entity.enums.InterfaceLanguage;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "text", schema = "public")
public class Text extends BaseEntity {

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "language", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private InterfaceLanguage language;

    @Column(name = "command_button")
    @Enumerated(EnumType.ORDINAL)
    private Command commandButton;

    @Column(name = "is_formatted")
    private Boolean isFormatted;

}
