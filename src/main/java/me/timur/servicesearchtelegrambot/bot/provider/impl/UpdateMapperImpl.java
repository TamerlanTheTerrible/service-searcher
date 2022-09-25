package me.timur.servicesearchtelegrambot.bot.provider.impl;

import me.timur.servicesearchtelegrambot.bot.provider.UpdateMapper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * Created by Temurbek Ismoilov on 25/09/22.
 */

@Component
public class UpdateMapperImpl implements UpdateMapper {
    @Override
    public List<BotApiMethod<Message>> map(Update update) {
        return null;
    }
}
