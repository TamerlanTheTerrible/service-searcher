package me.timur.servicesearchtelegrambot.bot.client.enums;

import lombok.Getter;

/**
 * Created by Temurbek Ismoilov on 19/08/22.
 */

@Getter
public enum Outcome {
    START("/start"),
    SERVICE_SEARCH_FOUND("Возможно вы имели ввиду... \nЕсли не нашли, то что искали напишите названия сервиса еще раз или выберите услугу из общего списка"),
    SERVICE_SEARCH_NOT_FOUND("Не удалось найти сервис. Попробуйте еще раз или выберите из списка"),
    QUERY_SAVED("Ваша заявка принято. С вами свяжутся, как только найдется нужный сервис провайдер"),
    CATEGORIES("Выбрать из общего списка"),
    SERVICES("Выбрать из общего списка"),
    ACCEPT("Принять"),
    DENY("Отказать"),
    BACK_TO_CATEGORIES("Все категории"),

    PROVIDER_SERVICE_ALREADY_EXISTS("Этот сервис у Вас уже зарегистрирован"),
    PROVIDER_SERVICE_SAVED("Сервис сохранён"),
    RECEIVE_QUERY_NOTIFICATION("Запрос"),

    UNKNOWN_COMMAND("Неизвестная команда. Попробуйте еще раз"),

    NEW_SEARCH("/search"),
    MY_QUERIES("/queries"),
    CHOOSE_QUERY("Выбрать запрос"),
    DEACTIVATE_QUERY("Закрыть запрос"),
    QUERY_DEACTIVATED("Запрос закрыт"),
    BACK_TO_MY_QUERIES("Вернуться к запросам"),
    QUERY_NOTIFIED("Запрос разослан"),
    PHONE_REQUESTED("Телефон запросан"),
    CONTACT_REQUESTED("Имя пользователя"),
    USERNAME("Имя пользователя"),
    PHONE("Телефон"),
    INVALID_PHONE_FORMAT_SENT("Неверный формат телефона, отправьте еще раз")
    ;

    private final String text;

    Outcome(String s) {
        this.text = s;
    }
}
