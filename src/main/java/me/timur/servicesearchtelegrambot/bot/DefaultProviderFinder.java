package me.timur.servicesearchtelegrambot.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.timur.servicesearchtelegrambot.enitity.Query;
import me.timur.servicesearchtelegrambot.service.QueryService;
import me.timur.servicesearchtelegrambot.service.ServiceProviderService;
import org.springframework.stereotype.Component;

/**
 * Created by Temurbek Ismoilov on 23/08/22.
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultProviderFinder implements ProviderFinder {

    private final QueryService queryService;
    private final ServiceProviderService providerService;

    @Override
    public void findProvider(Query query) {
        
    }
}
