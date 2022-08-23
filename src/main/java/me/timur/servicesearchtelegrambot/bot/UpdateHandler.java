package me.timur.servicesearchtelegrambot.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Created by Temurbek Ismoilov on 23/08/22.
 */

public interface UpdateHandler {
    SendMessage start(Update update);

    SendMessage searchService(Update update);

    SendMessage saveQueryIfServiceFoundOrSearchFurther(Update update);

    SendMessage unknownCommand(Update update);
}
