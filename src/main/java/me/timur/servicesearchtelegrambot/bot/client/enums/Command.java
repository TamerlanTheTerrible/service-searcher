package me.timur.servicesearchtelegrambot.bot.client.enums;

import lombok.Getter;

/**
 * Created by Temurbek Ismoilov on 06/08/22.
 */

@Getter
public enum Command {
    START("/start"),
    NEW_SEARCH("/search"),
    NEW_SEARCH_BUTTON("\uD83D\uDD0E Новый поиск"),
    MY_QUERIES("/queries"),
    MY_QUERIES_BUTTON("\uD83D\uDDC2 Мои запросы"),
  ;
    private final String text;

    Command(String s) {
        this.text = s;
    }

}
