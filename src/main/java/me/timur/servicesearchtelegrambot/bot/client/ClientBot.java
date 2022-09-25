package me.timur.servicesearchtelegrambot.bot.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

/**
 * Created by Temurbek Ismoilov on 13/08/22.
 */

@Component
@RequiredArgsConstructor
public class ClientBot extends TelegramLongPollingBot {

    private final UpdateMapper updateMapper;

    private final static String BOT_NAME = "UzservicesearchBot";
    private final static String BOT_TOKEN = "5452269303:AAGodrX6ZbfbFfo5GNkOK2CArsmyAqpdeyE";

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            handle(update);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handle(Update update) throws TelegramApiException {
        final List<BotApiMethod<Message>> sendMessageList = updateMapper.map(update);
        for (BotApiMethod<Message> message: sendMessageList) {
            execute(message);
        }
    }

}
