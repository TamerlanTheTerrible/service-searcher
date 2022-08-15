package me.timur.servicesearchtelegrambot.bot.util;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;
import java.util.Objects;

/**
 * Created by Temurbek Ismoilov on 15/08/22.
 */

public class UpdateUtil {

    public static String command(Update update) {
        return Objects.nonNull(update.getMessage())
                ? update.getMessage().getText()
                : update.getCallbackQuery().getMessage().getText();
    }

    public static String chatId(Update update) {
        return update.getMessage().getChatId().toString();
    }

    public static Long tgUserId(Update update) {
        return update.getMessage().getFrom().getId();
    }

    public static  SendMessage message(String chatId, String text) {
        return new SendMessage(chatId, text);
    }

    public static  SendMessage keyboard(String chatId, List<String> serviceNames, Integer keyboardRowSize) {
        SendMessage sendMessage;
        final ReplyKeyboardMarkup keyboard = KeyboardUtil.keyboard(serviceNames, keyboardRowSize);
        sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text("ulalala")
                .replyMarkup(keyboard)
                .build();
        return sendMessage;
    }
}
