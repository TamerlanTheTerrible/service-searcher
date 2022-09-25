package me.timur.servicesearchtelegrambot.bot.provider;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * Created by Temurbek Ismoilov on 23/08/22.
 */

public interface UpdateHandler {
    SendMessage start(Update update);
}
