package me.timur.servicesearchtelegrambot.model.enums;

import lombok.Getter;

/**
 * Created by Temurbek Ismoilov on 06/08/22.
 */

@Getter
public enum Command {
    START("/start");

    private final String value;

    Command(String s) {
        this.value = s;
    }

}
