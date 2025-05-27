package com.muzaffar.masjidfinder.service.cache.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class AdminModesDTO {

    private String chatId;
    private AdminMode adminModes;
    private Long masjidId;

    //    private Boolean searchMode;
    //    private Integer attempts;
    //    private LocalDateTime restartTime;

}
