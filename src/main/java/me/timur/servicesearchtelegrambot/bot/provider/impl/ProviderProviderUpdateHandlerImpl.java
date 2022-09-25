package me.timur.servicesearchtelegrambot.bot.provider.impl;

import me.timur.servicesearchtelegrambot.bot.provider.ProviderUpdateHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Created by Temurbek Ismoilov on 25/09/22.
 */

@Component
public class ProviderProviderUpdateHandlerImpl implements ProviderUpdateHandler {
    @Override
    public SendMessage start(Update update) {
        return null;
    }

    @Override
    public SendMessage unknownCommand(Update update) {
        return null;
    }
}
