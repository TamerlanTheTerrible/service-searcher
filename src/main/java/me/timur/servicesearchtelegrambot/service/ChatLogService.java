package me.timur.servicesearchtelegrambot.service;

import com.pengrad.telegrambot.model.Chat;

/**
 * Created by Temurbek Ismoilov on 06/08/22.
 */

public interface ChatLogService {
    void log(Chat chat);
    String getLastChatCommand(Chat chat);
}
