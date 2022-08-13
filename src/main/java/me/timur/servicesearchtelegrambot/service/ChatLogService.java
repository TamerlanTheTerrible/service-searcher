package me.timur.servicesearchtelegrambot.service;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;

/**
 * Created by Temurbek Ismoilov on 06/08/22.
 */

public interface ChatLogService {
    void log(Update update, Chat chat);
    String getLastChatCommand(Chat chat);
}
