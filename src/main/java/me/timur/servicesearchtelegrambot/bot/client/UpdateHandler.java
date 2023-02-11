package me.timur.servicesearchtelegrambot.bot.client;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Temurbek Ismoilov on 23/08/22.
 */

public interface UpdateHandler {
    SendMessage start(Update update);

    List<SendMessage> addQueryCommentAndRequestContact(Update update);

    List<SendMessage> sendNotificationIfUsernamePresent(Update update);

    SendMessage requestPhone(Update update);

    List<SendMessage> savePhone(Update update);

    List<SendMessage> sendNewQueryNotifications(Update update);

    SendMessage saveRegion(Update update);

    SendMessage searchNewService(Update update);

    SendMessage cancel(Update update);

    SendMessage searchWithOptions(Update update);

    SendMessage saveRegionAndRequestService(Update update);

    SendMessage saveQueryIfServiceFoundOrSearchFurther(Update update);

    SendMessage unknownCommand(Update update);

    SendMessage getServicesByCategoryName(Update update);

    SendMessage getCategories(Update update);

    SendMessage getUserQueries(Update update);

    SendMessage getQueryById(Update update);

    SendMessage deactivateQuery(Update update);

    SendMessage test(Update update);

    SendMessage publicOffer(Update update);

    SendMessage editRegion(Update update);

    List<SendMessage> back(Update update);

    List<SendMessage> settingsMenu(Update update);

    SendMessage closeActiveQueries(Update update);
}
