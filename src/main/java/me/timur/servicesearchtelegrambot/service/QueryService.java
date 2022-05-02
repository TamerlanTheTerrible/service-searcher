package me.timur.servicesearchtelegrambot.service;

import me.timur.servicesearchtelegrambot.model.QueryDto;

/**
 * Created by Temurbek Ismoilov on 24/04/22.
 */

public interface QueryService {
    void save(QueryDto dto);
}
