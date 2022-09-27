package me.timur.servicesearchtelegrambot.bot.client;

import me.timur.servicesearchtelegrambot.enitity.Query;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

/**
 * Created by Temurbek Ismoilov on 27/08/22.
 */

public interface ProviderNotifier {
    List<SendMessage> notifyProviders(Query query);
    SendMessage sendToTheGroup(Query query);
}
