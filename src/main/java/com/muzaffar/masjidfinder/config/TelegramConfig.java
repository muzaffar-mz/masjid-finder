package com.muzaffar.masjidfinder.config;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * Created on 24/12/24.
 */

@Configuration
@RequiredArgsConstructor
public class TelegramConfig {

    @Value("${telegram.user.token}")
    private String tokenUser;

    @Value("${telegram.admin.token}")
    private String tokenAdmin;

    @Bean
    public TelegramClient telegramClient() {
        return new OkHttpTelegramClient(tokenUser);
    }

    @Bean
    public TelegramClient telegramAdminClient() {
        return new OkHttpTelegramClient(tokenAdmin);
    }

}
