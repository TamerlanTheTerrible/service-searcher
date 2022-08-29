package me.timur.servicesearchtelegrambot.bot.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.timur.servicesearchtelegrambot.bot.util.UpdateUtil;
import me.timur.servicesearchtelegrambot.enitity.Query;
import me.timur.servicesearchtelegrambot.enitity.Service;
import me.timur.servicesearchtelegrambot.enitity.ServiceProvider;
import me.timur.servicesearchtelegrambot.bot.ProviderNotifier;
import me.timur.servicesearchtelegrambot.enitity.User;
import me.timur.servicesearchtelegrambot.service.ServiceProviderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Temurbek Ismoilov on 27/08/22.
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class ProviderNotifierImpl implements ProviderNotifier {

    private final ServiceProviderService providerService;

    @Value("${keyboard.size.row}")
    private Integer keyboardRowSize;

    @Override
    public List<SendMessage> notifyProviders(Query query) {
        final List<ServiceProvider> providers = findProvidersByService(query.getService());
        log.info(String.format("Query %s providers -> %s", query.getId(), providers.stream().map(ServiceProvider::getId).toList()));

        List<SendMessage> messages = new ArrayList<>();
        for (ServiceProvider provider: providers) {
            try {
                SendMessage message = prepareMessage(provider, query);
                messages.add(message);
            } catch (Exception e) {
                log.error("Error while preparing notification for provider #" + provider.getId(), e);
            }
        }

        return messages;
    }

    private SendMessage prepareMessage(ServiceProvider provider, Query query) {
        final User user = provider.getUser();
        if (user == null) {
            return null;
        }

        final User client = query.getClient();
        String clientContact = Objects.nonNull(client.getUsername()) ? client.getUsername() : client.getPhone();
        return UpdateUtil.message(user.getTelegramId().toString(),"Новый запрос от " + clientContact);
    }

    private List<ServiceProvider> findProvidersByService(Service service) {
        if (service == null) {
            return new ArrayList<>();
        }
        return providerService.findAllByService(service);
    }
}
