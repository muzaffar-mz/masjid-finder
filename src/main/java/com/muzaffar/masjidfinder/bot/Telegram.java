package com.muzaffar.masjidfinder.bot;

import com.muzaffar.masjidfinder.service.cache.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.muzaffar.masjidfinder.bot.util.UpdateUtil.isCallbackQuery;

@Slf4j
@Component
@RequiredArgsConstructor
public class Telegram implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;
    private final UpdateMapper updateMapper;
    private final CacheService cacheService;

    @Value("${telegram.token}")
    private String token;

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        try {
            handle(update);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handle(Update update) throws TelegramApiException {

        //1. handles update
        final var sendMessages = updateMapper.map(update);

        //2. deletes all previous messages
        var chatId = ((SendMessage) sendMessages.getFirst()).getChatId();
        deleteMessages(chatId);
        deleteSentUpdate(chatId, update);

        //3. sends all new messages
        executeMessages(sendMessages, chatId);
    }

    private void deleteSentUpdate(String chatId, Update update) {
        if (isCallbackQuery(update) ||
                (Objects.nonNull(update.getMessage().getText()) && update.getMessage().getText().equals("/start"))) {
            return;
        }

        try {
            telegramClient.execute(new DeleteMessage(chatId, update.getMessage().getMessageId()));
        } catch (TelegramApiException e) {
            log.error("Error while deleting sent update. Chat ID: {}, message: {}, exception: {}",
                    chatId, update.getMessage().getMessageId(), e.toString());
        }
    }

    private void executeMessages(List<PartialBotApiMethod<?>> sendMessages, String chatId) {

        //to store sent message id
        List<Integer> sentMessagesId = new ArrayList<>();

        //send message
        sendMessages.forEach(method -> {
            try {
                var repl = telegramClient.execute((BotApiMethod<?>) method);
                if (repl instanceof Message message) {
                    sentMessagesId.add(message.getMessageId());
                }
            } catch (TelegramApiException e) {
                var message = (SendMessage) method;
                log.error("Error while sending the message. Chat ID: {}, message: {}, exception: {}",
                        chatId, message, e.toString());
            }
        });

        cacheService.saveSentMessagesId(chatId, sentMessagesId);
    }

    private void deleteMessages(String chatId) {
        var messages = cacheService.getMessagesIdByChatId(chatId);

        if (Objects.isNull(messages)) {
            return;
        }

        messages.forEach(i -> {
            try {
                telegramClient.execute(new DeleteMessage(chatId, i));
            } catch (TelegramApiException e) {
                log.error("Error while deleting the message. Chat ID: {}, message id: {}, exception: {}",
                        chatId, i, e.toString());
            }
        });
    }

}
