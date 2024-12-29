package com.muzaffar.masjidfinder.bot.util;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public class UpdateUtil {

    public static SendMessage getSendMessage(String chatId, String text, ReplyKeyboardMarkup keyboard) {
        var senMessage = message(chatId, text);
        senMessage.setReplyMarkup(keyboard);
        return senMessage;
    }

    public static SendMessage inLineKeyboard(String chatId, String text, InlineKeyboardMarkup keyboard) {
        var sm = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(keyboard)
                .build();
        return sm;
    }

    public static SendMessage getSendMessage(String chatId, String text, InlineKeyboardMarkup keyboard) {
        var sendMessage = message(chatId, text);
        sendMessage.setReplyMarkup(keyboard);
        return sendMessage;
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

    public static Integer messageId(Update update) {
        return update.hasMessage()
                ? update.getMessage().getMessageId()
                : update.getCallbackQuery().getMessage().getMessageId();
    }

    public static Long getMasjidId(Update update) {
        final String data = update.getCallbackQuery().getData();
        int index = data.lastIndexOf('_');
        return Long.parseLong(data.substring(index + 1));
    }
}
