package com.muzaffar.masjidfinder.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class Telegram implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;

    @Override
    public void consume(Update update) {
        System.out.println(update.toString());
    }
}
