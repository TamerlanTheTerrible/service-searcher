package me.timur.servicesearchtelegrambot.bot.provider.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.timur.servicesearchtelegrambot.bot.provider.ProviderUpdateHandler;
import me.timur.servicesearchtelegrambot.bot.provider.ProviderUpdateMapper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Temurbek Ismoilov on 25/09/22.
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class ProviderUpdateMapperImpl implements ProviderUpdateMapper {

    private final ProviderUpdateHandler updateHandler;

    @Override
    public List<BotApiMethod<Message>> map(Update update) {
        final List<BotApiMethod<Message>> replyList = new ArrayList<>();

        SendMessage sendMessage = tryToMap(update);

        if (sendMessage != null)
            replyList.add(sendMessage);

        return replyList;
    }

    private SendMessage tryToMap(Update update) {
        SendMessage sendMessage;
        try {
            sendMessage = updateHandler.start(update);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            sendMessage = updateHandler.unknownCommand(update);
        }
        return sendMessage;
    }
}
