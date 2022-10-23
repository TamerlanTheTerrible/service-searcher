package me.timur.servicesearchtelegrambot.bot.client;

import me.timur.servicesearchtelegrambot.enitity.Query;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * Created by Temurbek Ismoilov on 23/08/22.
 */

public interface UpdateHandler {
    SendMessage start(Update update);

    List<SendMessage> savePhone(Update update);

    List<SendMessage> sendNewQueryNotifications(Update update);

    SendMessage searchService(Update update);

    List<SendMessage> saveQueryIfServiceFoundOrSearchFurther(Update update);

    SendMessage unknownCommand(Update update);

    SendMessage getServicesByCategoryName(Update update);

    SendMessage getCategories(Update update);

    SendMessage getUserQueries(Update update);

    SendMessage getQueryById(Update update);

    SendMessage deactivateQuery(Update update);
}
