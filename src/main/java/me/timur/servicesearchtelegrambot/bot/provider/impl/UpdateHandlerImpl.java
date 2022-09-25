package me.timur.servicesearchtelegrambot.bot.provider.impl;

import me.timur.servicesearchtelegrambot.bot.provider.UpdateHandler;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Created by Temurbek Ismoilov on 25/09/22.
 */

public class UpdateHandlerImpl implements UpdateHandler {
    @Override
    public SendMessage start(Update update) {
        return null;
    }
}
