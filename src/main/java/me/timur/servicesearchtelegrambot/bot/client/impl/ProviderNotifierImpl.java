package me.timur.servicesearchtelegrambot.bot.client.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.timur.servicesearchtelegrambot.bot.util.UpdateUtil;
import me.timur.servicesearchtelegrambot.enitity.Query;
import me.timur.servicesearchtelegrambot.enitity.Service;
import me.timur.servicesearchtelegrambot.enitity.Provider;
import me.timur.servicesearchtelegrambot.bot.client.ProviderNotifier;
import me.timur.servicesearchtelegrambot.enitity.User;
import me.timur.servicesearchtelegrambot.service.ProviderManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Temurbek Ismoilov on 27/08/22.
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class ProviderNotifierImpl implements ProviderNotifier {

    @Value("${channel.service.searcher.id.dev}")
    private String serviceSearchChannelId;

    private final ProviderManager providerManager;

    @Override
    public List<SendMessage> notifyProviders(Query query) {
        final List<Provider> providers = findProvidersByService(query.getService());
        log.info(String.format("Query %s providers -> %s", query.getId(), providers.stream().map(Provider::getId).collect(Collectors.toList())));

        List<SendMessage> messages = new ArrayList<>();
        for (Provider provider: providers) {
            try {
                SendMessage message = prepareMessage(provider, query);
                messages.add(message);
            } catch (Exception e) {
                log.error("Error while preparing notification for provider #" + provider.getId(), e);
            }
        }

        return messages;
    }

    @Override
    public SendMessage sendToTheGroup(Query query) {
        String message = String.format("Новый запрос #%s на %s", query.getId(), query.getService().getName());
        return UpdateUtil.message(serviceSearchChannelId, message);
    }

    private SendMessage prepareMessage(Provider provider, Query query) {
        final User user = provider.getUser();
        if (user == null) {
            return null;
        }

        final User client = query.getClient();
        String clientContact = Objects.nonNull(client.getUsername()) ? client.getUsername() : client.getPhone();
        String message = String.format("Новый запрос от %s на %s", clientContact, query.getService().getName());
        return UpdateUtil.message(provider.getUser().getTelegramId().toString(), message);
    }

    private List<Provider> findProvidersByService(Service service) {
        if (service == null) {
            return new ArrayList<>();
        }
        return providerManager.findAllByService(service);
    }
}
