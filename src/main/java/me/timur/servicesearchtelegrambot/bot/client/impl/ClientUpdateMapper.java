package me.timur.servicesearchtelegrambot.bot.client.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.timur.servicesearchtelegrambot.bot.client.UpdateHandler;
import me.timur.servicesearchtelegrambot.bot.client.UpdateMapper;
import me.timur.servicesearchtelegrambot.bot.ChatLogType;
import me.timur.servicesearchtelegrambot.bot.client.enums.Command;
import me.timur.servicesearchtelegrambot.bot.client.enums.Outcome;
import me.timur.servicesearchtelegrambot.service.ChatLogService;
import me.timur.servicesearchtelegrambot.service.ServiceManager;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
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
public class ClientUpdateMapper implements UpdateMapper {

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
            final String newCommand = command(update) != null ? command(update) : "";
            final String lastChatCommand = chatLogService.getLastChatOutcome(update, ChatLogType.CLIENT) != null
                    ? chatLogService.getLastChatOutcome(update, ChatLogType.CLIENT)
                    : "";

            // start command called
            if (Objects.equals(newCommand, Command.START.getText()))
                sendMessage = updateHandler.start(update);
            else if (Objects.equals(newCommand, "/test"))
                sendMessage = updateHandler.test(update);
            else if (Objects.equals(newCommand, Command.SETTINGS.getText()))
                replyList.addAll(updateHandler.settingsMenu(update));
            else if (Objects.equals(newCommand, Command.BACK.getText()))
                replyList.addAll(updateHandler.back(update));
            else if (Objects.equals(newCommand, Outcome.CANCEL.getText()))
                sendMessage = updateHandler.cancel(update);
            // new search command
            else if (newCommand.equals(Command.NEW_SEARCH.getText()) || newCommand.equals(Command.NEW_SEARCH_BUTTON.getText()))
                sendMessage = updateHandler.searchNewService(update);
            // public offer
            else if (newCommand.equals(Command.OFFER.getText()) || newCommand.equals(Command.OFFER_BUTTON.getText()))
                sendMessage = updateHandler.publicOffer(update);
            // region requested
            else if (lastChatCommand.equals(Outcome.REGION_REQUESTED.name()))
                sendMessage = updateHandler.saveRegionAndRequestService(update);
            // send notifications if username exists
            else if (Objects.equals(newCommand, Outcome.USERNAME.getText()))
                replyList.addAll( updateHandler.sendNotificationIfUsernamePresent(update));
            // request phone
            else if (Objects.equals(newCommand, Outcome.PHONE.getText()))
                sendMessage = updateHandler.requestPhone(update);
            //save phone
            else if (update.getMessage() != null && update.getMessage().getContact() != null &&  update.getMessage().getContact().getPhoneNumber() != null)
                replyList.addAll(updateHandler.savePhone(update));
            // get all user queries
            else if (newCommand.equals(Command.MY_QUERIES.getText()) || newCommand.equals(Outcome.BACK_TO_MY_QUERIES.getText()) || newCommand.equals(Command.MY_QUERIES_BUTTON.getText()))
                sendMessage = updateHandler.getUserQueries(update);
            else if (newCommand.equals(Command.MY_QUERIES_CLOSE_ALL.getText()))
                sendMessage = updateHandler.closeActiveQueries(update);
                // choose active query
            else if (lastChatCommand.equals(Outcome.MY_QUERIES.name()) && newCommand.startsWith("#"))
                sendMessage = updateHandler.getQueryById(update);
                // deactivate query
            else if (newCommand.contains(Outcome.DEACTIVATE_QUERY.getText()))
                sendMessage = updateHandler.deactivateQuery(update);
            // list of all services required
            else if (Objects.equals(newCommand, Outcome.CATEGORIES.getText()) || Objects.equals(newCommand, Outcome.BACK_TO_CATEGORIES.getText()) ) {
                sendMessage = updateHandler.getCategories(update);
            }
            // list of all services required
            else if (lastChatCommand.equals(Outcome.CATEGORIES.name()) || lastChatCommand.equals(Outcome.BACK_TO_CATEGORIES.name())) {
                sendMessage = updateHandler.getServicesByCategoryName(update);
            }
            // required service found
            else if ((lastChatCommand.equals(Outcome.SERVICES.name()) || lastChatCommand.equals(Outcome.SERVICE_SEARCH_FOUND.name())) && serviceNames.contains(newCommand))
                sendMessage = updateHandler.saveQueryIfServiceFoundOrSearchFurther(update);
            // query comment requested
            else if (lastChatCommand.equals(Outcome.QUERY_COMMENT_REQUESTED.name())) {
                replyList.addAll(updateHandler.addQueryCommentAndRequestContact(update));
            }
            // searching a service
            else if (lastChatCommand.equals(Outcome.NEW_SEARCH.name())  || lastChatCommand.equals(Outcome.SERVICE_SEARCH_NOT_FOUND.name()) || lastChatCommand.equals(Outcome.SERVICE_SEARCH_FOUND.name()))
                sendMessage = updateHandler.searchWithOptions(update);
            // edit region
            else if (newCommand.contains("➕ Регион") || newCommand.contains("✏️ Регион:")) {
                sendMessage = updateHandler.editRegion(update);
            }
            // save region
            else if (lastChatCommand.equals(Outcome.REGION_EDIT_REQUESTED.name())) {
                sendMessage = updateHandler.saveRegion(update);
            }
            else
                sendMessage = updateHandler.unknownCommand(update);
        } catch (Exception e) {
            log.error("ERROR --- user: {} command: {} msg: {}", chatId(update), (command(update) != null ? command(update): "null command"), e.getMessage(), e);
            sendMessage = updateHandler.unknownCommand(update);
        }
        return sendMessage;
    }
}
