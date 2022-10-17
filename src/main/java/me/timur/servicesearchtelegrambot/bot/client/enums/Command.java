package me.timur.servicesearchtelegrambot.bot.client.enums;

import lombok.Getter;

/**
 * Created by Temurbek Ismoilov on 06/08/22.
 */

@Getter
public enum Command {
    START("/start"),
    NEW_SEARCH("/search"),
    MY_QUERIES("/queries"),
    ;
    private final String value;

    Command(String s) {
        this.value = s;
    }

}
