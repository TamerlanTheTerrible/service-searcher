package me.timur.servicesearchtelegrambot.bot;

import me.timur.servicesearchtelegrambot.enitity.Query;
import org.springframework.scheduling.annotation.Async;

/**
 * Created by Temurbek Ismoilov on 23/08/22.
 */

public interface ProviderFinder {
    @Async
    void findProvider(Query query);
}
