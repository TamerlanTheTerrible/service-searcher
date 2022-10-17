package me.timur.servicesearchtelegrambot.bot.client.impl;

import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.bot.client.ProviderNotifier;
import me.timur.servicesearchtelegrambot.bot.client.UpdateHandler;
import me.timur.servicesearchtelegrambot.bot.client.enums.Command;
import me.timur.servicesearchtelegrambot.enitity.Query;
import me.timur.servicesearchtelegrambot.enitity.Service;
import me.timur.servicesearchtelegrambot.enitity.User;
import me.timur.servicesearchtelegrambot.bot.client.enums.Outcome;
import me.timur.servicesearchtelegrambot.model.dto.UserDTO;
import me.timur.servicesearchtelegrambot.service.ChatLogService;
import me.timur.servicesearchtelegrambot.service.QueryService;
import me.timur.servicesearchtelegrambot.service.ServiceManager;
import me.timur.servicesearchtelegrambot.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static me.timur.servicesearchtelegrambot.bot.util.UpdateUtil.*;
import static me.timur.servicesearchtelegrambot.bot.util.UpdateUtil.chatId;

/**
 * Created by Temurbek Ismoilov on 23/08/22.
 */

@Component
@RequiredArgsConstructor
public class DefaultUpdateHandler implements UpdateHandler {

    @Value("${keyboard.size.row}")
    private Integer keyboardRowSize;

    private final ServiceManager serviceManager;
    private final QueryService queryService;
    private final UserService userService;
    private final ChatLogService chatLogService;
    private final ProviderNotifier providerNotifier;

    @Override
    public List<SendMessage> saveQueryIfServiceFoundOrSearchFurther(Update update) {
        Service service = serviceManager.getServiceByName(command(update));
        List<SendMessage> messages = new ArrayList<>();
        if (service == null) {
            messages.add(searchService(update));
        } else {
            //save query
            User client = userService.getOrSave(user(update));
            Query query = new Query(client, service);
            queryService.save(query);
            //prepare message for client
            SendMessage clientMsg = logAndMessage(update,  Outcome.QUERY_SAVED.getText(), Outcome.QUERY_SAVED);
            clientMsg.setReplyMarkup(removeKeyboard());
            messages.add(clientMsg);
            //prepare messages for providers
            SendMessage groupMessage = providerNotifier.sendToTheGroup(query);
            messages.add(groupMessage);
        }
        return messages;
    }

    @Override
    public SendMessage searchService(Update update) {
        String command = command(update);
        SendMessage sendMessage;

        final List<Service> services = serviceManager.getAllServicesByActiveTrueAndNameLike(command);
        if (services.isEmpty()) {
            List<String> keyboardValues = new ArrayList<>();
            keyboardValues.add(Outcome.CATEGORIES.getText());
            sendMessage = logAndMessage(update, Outcome.SERVICE_SEARCH_NOT_FOUND.getText(), Outcome.SERVICE_SEARCH_NOT_FOUND);
            sendMessage.setReplyMarkup(keyboard(keyboardValues,keyboardRowSize));
        } else {
            final List<String> serviceNames = services.stream().map(Service::getNameUz).collect(Collectors.toList());
            serviceNames.add(Outcome.CATEGORIES.getText());
            sendMessage = logAndKeyboard(update, Outcome.SERVICE_SEARCH_FOUND.getText(),  serviceNames, keyboardRowSize, Outcome.SERVICE_SEARCH_FOUND);
        }

        return sendMessage;
    }

    @Override
    public SendMessage start(Update update) {
        //save if user doesn't exist
        User client = userService.getOrSave(user(update));
        final String name = Objects.nonNull(client.getFirstname()) ? client.getFirstname()
                : Objects.nonNull(client.getLastname()) ? client.getLastname()
                : Objects.nonNull(client.getUsername()) ? client.getUsername()
                : "друг";
        final SendMessage sendMessage = logAndMessage(update, String.format("Добро пожаловать, %s. Напишите названия сервиса, который вы ищите", name), Outcome.START);
        sendMessage.setReplyMarkup(removeKeyboard());
        return sendMessage;
    }

    @Override
    public SendMessage getServicesByCategoryName(Update update) {
        List<String> servicesNames = serviceManager.getServicesNamesByCategoryName(command(update));
        ArrayList<String> modifiableList = new ArrayList<>(servicesNames);
        modifiableList.add(Outcome.BACK_TO_CATEGORIES.getText());
        return logAndKeyboard(update, command(update), modifiableList, keyboardRowSize, Outcome.SERVICES);
    }

    @Override
    public SendMessage getCategories(Update update) {
        final List<String> categoryNames = serviceManager.getActiveCategoryNames();
        return logAndKeyboard(update, Outcome.CATEGORIES.getText(), categoryNames, keyboardRowSize, Outcome.CATEGORIES);
    }

    @Override
    public SendMessage getUserQueries(Update update) {
        final UserDTO userDTO = user(update);
        List<String> queryNames = queryService
                .getAllActiveByClientTgId(userDTO.getTelegramId())
                .stream().map(q -> "#" + q.getId() +" : " + q.getService().getName())
                .collect(Collectors.toList());
        return logAndKeyboard(update, Command.MY_QUERIES.getValue(), queryNames, 1, Outcome.MY_QUERIES);
    }

    @Override
    public SendMessage unknownCommand(Update update) {
        return logAndMessage(update, Outcome.UNKNOWN_COMMAND.getText(), Outcome.UNKNOWN_COMMAND);
    }

    private SendMessage logAndMessage(Update update, String message, Outcome outcome) {
        chatLogService.log(update, outcome);
        return message(chatId(update), message);
    }

    private SendMessage logAndKeyboard(Update update, String message, List<String> serviceNames, Integer keyboardRowSize, Outcome outcome) {
        chatLogService.log(update, outcome);
        return keyboard(chatId(update), message, serviceNames, keyboardRowSize);
    }
}
