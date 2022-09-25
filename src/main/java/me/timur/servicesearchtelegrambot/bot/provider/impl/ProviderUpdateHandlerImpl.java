package me.timur.servicesearchtelegrambot.bot.provider.impl;

import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.bot.client.enums.Outcome;
import me.timur.servicesearchtelegrambot.bot.provider.ProviderUpdateHandler;
import me.timur.servicesearchtelegrambot.enitity.Provider;
import me.timur.servicesearchtelegrambot.service.ChatLogService;
import me.timur.servicesearchtelegrambot.service.ProviderManager;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static me.timur.servicesearchtelegrambot.bot.util.UpdateUtil.*;
import static me.timur.servicesearchtelegrambot.bot.util.UpdateUtil.chatId;

/**
 * Created by Temurbek Ismoilov on 25/09/22.
 */

@Component
@RequiredArgsConstructor
public class ProviderUpdateHandlerImpl implements ProviderUpdateHandler {

    private final ProviderManager providerManager;
    private final ChatLogService chatLogService;

    @Override
    public SendMessage start(Update update) {
        //save if user doesn't exist
        Provider provider = providerManager.getOrSave(user(update));
        final String name = provider.getName();
        final SendMessage sendMessage = logAndMessage(update, String.format("Добро пожаловать, %s. Напишите названия сервиса, который вы ищите", name), Outcome.START);
        sendMessage.setReplyMarkup(removeKeyboard());
        return sendMessage;
    }

    @Override
    public SendMessage unknownCommand(Update update) {
        return null;
    }

    private SendMessage logAndMessage(Update update, String message, Outcome outcome) {
        chatLogService.log(update, outcome);
        return message(chatId(update), message);
    }

    private SendMessage logAndKeyboard(Update update, String message, List<String> serviceNames, Integer keyboardRowSize, Outcome outcome) {
        chatLogService.log(update, outcome);
        return keyboard(chatId(update), message, serviceNames, keyboardRowSize);
    }
}
