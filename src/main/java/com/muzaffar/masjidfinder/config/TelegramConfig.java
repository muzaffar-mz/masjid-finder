package com.muzaffar.masjidfinder.config;


import com.muzaffar.masjidfinder.bot.UpdateMapper;
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

    @Value("${telegram.token}")
    private String token;

    @Bean
    public TelegramClient telegramClient() {
        return new OkHttpTelegramClient(token);
    }

}
