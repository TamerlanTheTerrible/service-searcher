package me.timur.servicesearchtelegrambot.service;

import me.timur.servicesearchtelegrambot.bot.ChatLogType;
import me.timur.servicesearchtelegrambot.bot.client.enums.Outcome;
import me.timur.servicesearchtelegrambot.enitity.ChatLog;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Created by Temurbek Ismoilov on 06/08/22.
 */

public interface ChatLogService {
//    void log(Update update, Outcome outcome);

    void log(Update update, Outcome outcome, ChatLogType logType);

//    void log(Update update, me.timur.servicesearchtelegrambot.bot.provider.enums.Outcome outcome);

    void log(Update update, me.timur.servicesearchtelegrambot.bot.provider.enums.Outcome outcome, ChatLogType logType);

    String getLastChatOutcome(Update update, ChatLogType client);

    ChatLog getLastByOutcome(String tgChatId, String command, ChatLogType logType);
}
