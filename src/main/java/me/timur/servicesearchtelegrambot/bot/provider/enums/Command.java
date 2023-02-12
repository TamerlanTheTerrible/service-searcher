package me.timur.servicesearchtelegrambot.bot.provider.enums;

import lombok.Getter;

/**
 * Created by Temurbek Ismoilov on 06/08/22.
 */

@Getter
public enum Command {
    START("/start"),
    SETTINGS("/info"),
    SETTINGS_BUTTON("⚙️ Настройки"),
    NEW_SERVICE("/newservice"),
    NEW_SERVICE_BUTTON("\uD83D\uDD0E Новая услуга"),
    MY_SERVICES("/services"),
    MY_SERVICES_BUTTON("Мои услуги"),
    GET_QUERIES("/requests"),
    GET_QUERIES_BUTTON("Актуальные запросы"),
    SUBSCRIBE_TO_SERVICE("➕ Подписаться"),
    UNSUBSCRIBE_FROM_SERVICE("\uD83D\uDDD1 Отписаться"),
    ACCEPT_QUERY("Принять запрос #"),
    DENY_QUERY("Отказать"),
    BACK_TO_CATEGORIES("Все категории"),
    OFFER("/oferta"),
    OFFER_BUTTON("\uD83D\uDCC4 Публичная оферта"),
    BACK("◀️ Назад"),
    ;

    private final String text;

    Command(String s) {
        this.text = s;
    }

}
