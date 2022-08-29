package me.timur.servicesearchtelegrambot.bot.enums;

import lombok.Getter;

/**
 * Created by Temurbek Ismoilov on 19/08/22.
 */

@Getter
public enum Outcome {
    START("/start"),
    SERVICE_SEARCH_FOUND("Результат поиска. Можете выбрать из этого списка или продолжать поиск. Также можете выбрать услугу из общего списка"),
    SERVICE_SEARCH_NOT_FOUND("Не удалось найти сервис. Попробуйте еще раз или выберите из списка"),
    QUERY_SAVED("Ваша заявка принято. С вами свяжутся, как только найдется нужный сервис провайдер"),
    CATEGORIES("Выбрать из общего списка"),
    SERVICES("Выбрать из общего списка"),
    ACCEPT("Принять"),
    DENY("Отказать"),
    BACK_TO_CATEGORIES("Все категории"),
    UNKNOWN_COMMAND("Неизвестная команда. Попробуйте еще раз");

    private final String text;

    Outcome(String s) {
        this.text = s;
    }
}
