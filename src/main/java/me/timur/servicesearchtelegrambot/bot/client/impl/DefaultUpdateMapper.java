package me.timur.servicesearchtelegrambot.bot.client.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.timur.servicesearchtelegrambot.bot.client.UpdateHandler;
import me.timur.servicesearchtelegrambot.bot.client.UpdateMapper;
import me.timur.servicesearchtelegrambot.bot.client.enums.Command;
import me.timur.servicesearchtelegrambot.bot.client.enums.Outcome;
import me.timur.servicesearchtelegrambot.service.ChatLogService;
import me.timur.servicesearchtelegrambot.service.ServiceManager;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultUpdateMapper implements UpdateMapper {

    private final ChatLogService chatLogService;
    private final UpdateHandler updateHandler;
    private final ServiceManager serviceManager;
    @Override
    public List<BotApiMethod<Message>> map(Update update) {
        final List<String> serviceNames = serviceManager.getActiveServiceNames();
        final List<BotApiMethod<Message>> replyList = new ArrayList<>();

        SendMessage sendMessage = tryToMap(update, serviceNames, replyList);

        if (sendMessage != null)
            replyList.add(sendMessage);

        return replyList;
    }

    private SendMessage tryToMap(Update update, List<String> serviceNames, List<BotApiMethod<Message>> replyList) {
        SendMessage sendMessage = null;
        try {
            final String newCommand = command(update);
            final String lastChatCommand = chatLogService.getLastChatOutcome(update);

            // start command called
            if (Objects.equals(newCommand, Command.START.getValue()))
                sendMessage = updateHandler.start(update);
            // list of all services required
            else if (Objects.equals(newCommand, Outcome.CATEGORIES.getText()) || Objects.equals(newCommand, Outcome.BACK_TO_CATEGORIES.getText()) ) {
                sendMessage = updateHandler.getCategories(update);
            }
            // list of all services required
            else if (lastChatCommand.equals(Outcome.CATEGORIES.name()) || lastChatCommand.equals(Outcome.BACK_TO_CATEGORIES.name())) {
                sendMessage = updateHandler.getServicesByCategoryName(update);
            }
            // required service found
            else if ((lastChatCommand.equals(Outcome.SERVICES.name()) || lastChatCommand.equals(Outcome.SERVICE_SEARCH_FOUND.name())) && serviceNames.contains(newCommand)) {
                List<SendMessage> messages = updateHandler.saveQueryIfServiceFoundOrSearchFurther(update);
                replyList.addAll(messages);
            }
            // searching a service
            else if (lastChatCommand.equals(Outcome.START.name()) || lastChatCommand.equals(Outcome.SERVICE_SEARCH_NOT_FOUND.name()) || lastChatCommand.equals(Outcome.SERVICE_SEARCH_FOUND.name()))
                sendMessage = updateHandler.searchService(update);

            // unknown command
            else
                sendMessage = updateHandler.unknownCommand(update);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            sendMessage = updateHandler.unknownCommand(update);
        }
        return sendMessage;
    }
}
