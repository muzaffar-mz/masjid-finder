package com.muzaffar.masjidfinder.config;


import com.muzaffar.masjidfinder.bot.Telegram;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * Created on 24/12/24.
 */

@Configuration
public class TelegramConfig {

    @Value("${telegram.token}")
    private String token;

    @Bean
    public TelegramBotsLongPollingApplication botsLongPollingApplication() {
        try {
            TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();
            botsApplication.registerBot(token, new Telegram());
            return botsApplication;
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public TelegramClient telegramClient() {
        return new OkHttpTelegramClient(token);
    }

    @Bean
    public Telegram telegram() {
        return new Telegram();
    }


}
