package me.timur.servicesearchtelegrambot.bot.imp;

import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.bot.UpdateHandler;
import me.timur.servicesearchtelegrambot.bot.UpdateMapper;
import me.timur.servicesearchtelegrambot.enitity.Query;
import me.timur.servicesearchtelegrambot.enitity.Service;
import me.timur.servicesearchtelegrambot.enitity.User;
import me.timur.servicesearchtelegrambot.model.enums.Command;
import me.timur.servicesearchtelegrambot.model.enums.Outcome;
import me.timur.servicesearchtelegrambot.service.ChatLogService;
import me.timur.servicesearchtelegrambot.service.QueryService;
import me.timur.servicesearchtelegrambot.service.ServiceManager;
import me.timur.servicesearchtelegrambot.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.timur.servicesearchtelegrambot.bot.util.UpdateUtil.*;

/**
 * Created by Temurbek Ismoilov on 13/08/22.
 */

@Component
@RequiredArgsConstructor
public class DefaultUpdateMapper implements UpdateMapper {

    private final ChatLogService chatLogService;
    private final UpdateHandler updateHandler;

    @Override
    public List<BotApiMethod<Message>> map(Update update) {
        final String newCommand = command(update);
        final String lastChatCommand = chatLogService.getLastChatOutcome(update);
        SendMessage sendMessage;

        if (Objects.equals(newCommand, Command.START.getValue()))
            sendMessage = updateHandler.start(update);
        else if (lastChatCommand == null || lastChatCommand.equals(Outcome.START.name()) || lastChatCommand.equals(Outcome.SERVICE_SEARCH_FAILED.name()))
            sendMessage = updateHandler.searchService(update);
        else if (lastChatCommand.equals(Outcome.SERVICE_SEARCH_SUCCESS.name()))
            sendMessage = updateHandler.saveQueryIfServiceFoundOrSearchFurther(update);
        else
            sendMessage = updateHandler.unknownCommand(update);

        final List<BotApiMethod<Message>> replyList = new ArrayList<>();
        replyList.add(sendMessage);
        return replyList;
    }
}
