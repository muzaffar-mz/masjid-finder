package com.muzaffar.masjidfinder.bot.util;

import com.muzaffar.masjidfinder.service.text.model.TextDTO;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.function.Predicate;

public class UpdateUtil {

    public static SendMessage sendMessage(String chatId, TextDTO text, ReplyKeyboardMarkup keyboard) {
        var senMessage = message(chatId, text.text());
        senMessage.setReplyMarkup(keyboard);
        senMessage.enableMarkdownV2(text.isFormatted());
        return senMessage;
    }

    public static SendMessage sendMessage(String chatId, String text, InlineKeyboardMarkup keyboard) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(keyboard)
                .build();
    }

    public static SendMessage message(String chatId, TextDTO text) {
        var message = message(chatId, text.text());
        message.enableMarkdownV2(text.isFormatted());
        return message;
    }

    public static SendMessage message(String chatId, String text) {
        return new SendMessage(chatId, text);
    }

    public static String messageCommand(Update update) {
        return update.hasMessage() ? update.getMessage().getText() : null;
    }

    public static boolean hasLocation(Update update) {
        return update.getMessage().hasLocation();
    }

    public static boolean isCallbackQuery(Update update) {
        return update.hasCallbackQuery();
    }

    static Predicate<Update> isCallbackQueryFun = Update::hasCallbackQuery;

    public static String getChatId(Update update) {
        return update.hasMessage()
                ? update.getMessage().getChatId().toString()
                : update.getCallbackQuery().getMessage().getChatId().toString();
    }

    public static String callbackCommand(Update update) {
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData() != null && !update.getCallbackQuery().getData().isEmpty()) {
            final String data = update.getCallbackQuery().getData();
            int index = data.indexOf('_');
            return data.substring(0, index);
        }
        return null;
    }

    public static Long getMasjidId(Update update) {
        final String data = update.getCallbackQuery().getData();
        int index = data.lastIndexOf('_');
        return Long.parseLong(data.substring(index + 1));
    }
}
