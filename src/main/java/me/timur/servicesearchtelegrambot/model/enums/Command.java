package me.timur.servicesearchtelegrambot.model.enums;

import lombok.Getter;

/**
 * Created by Temurbek Ismoilov on 06/08/22.
 */

@Getter
public enum Command {
    START("/start");

    private final String stringValue;

    Command(String s) {
        this.stringValue = s;
    }

}
