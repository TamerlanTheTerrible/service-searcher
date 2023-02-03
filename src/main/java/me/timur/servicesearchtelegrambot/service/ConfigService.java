package me.timur.servicesearchtelegrambot.service;

import me.timur.servicesearchtelegrambot.bot.ConfigName;
import me.timur.servicesearchtelegrambot.enitity.Config;

/**
 * Created by Temurbek Ismoilov on 01/02/23.
 */

public interface ConfigService {
    Config getByName(ConfigName name);
    boolean containsBannedWord(String wordToCheck);
}
