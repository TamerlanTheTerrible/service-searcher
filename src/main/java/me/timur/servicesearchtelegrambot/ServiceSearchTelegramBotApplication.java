package me.timur.servicesearchtelegrambot;

import com.github.kshashov.telegram.api.MessageType;
import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.BotRequest;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.bot.BotService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@BotController
@RequiredArgsConstructor
@SpringBootApplication
public class ServiceSearchTelegramBotApplication implements TelegramMvcController {

    private final BotService botService;

    public static void main(String[] args) {
        SpringApplication.run(ServiceSearchTelegramBotApplication.class, args);
    }

    @Override
    public String getToken() {
        return botService.token();
    }

    @BotRequest(value = "/start", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest start(Update update, Chat chat) {
        return botService.start(update, chat);
    }

    @BotRequest(type = {MessageType.MESSAGE})
    public ReplyKeyboardMarkup anyText(Chat chat, Update update) {
        return botService.anyText(chat, update);
    }

}
