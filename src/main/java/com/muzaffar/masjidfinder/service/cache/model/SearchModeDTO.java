package com.muzaffar.masjidfinder.service.cache.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class SearchModeDTO {

    private String chatId;
    private Boolean searchMode;
    private Integer attempts;
    private LocalDateTime restartTime;

    public SearchModeDTO(String chatId) {
        this.chatId = chatId;
        this.attempts = 0;
        this.searchMode = true;
        this.restartTime = LocalDateTime.now().plusMinutes(30L);
    }

    public void restart() {
        this.attempts = 0;
        this.searchMode = true;
        this.restartTime = LocalDateTime.now().plusMinutes(30L);
    }

    public void incrementAttempts() {
        this.attempts++;
        this.searchMode = true;
    }

    public void turnOffSearchMode() {
        this.searchMode = false;
    }

    public boolean isSearchModeOn() {
        return this.searchMode;
    }
}
