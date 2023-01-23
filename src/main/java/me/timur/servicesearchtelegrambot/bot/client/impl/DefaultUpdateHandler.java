package me.timur.servicesearchtelegrambot.bot.client.impl;

import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.bot.client.ProviderNotifier;
import me.timur.servicesearchtelegrambot.bot.client.UpdateHandler;
import me.timur.servicesearchtelegrambot.bot.client.enums.Outcome;
import me.timur.servicesearchtelegrambot.bot.client.enums.Region;
import me.timur.servicesearchtelegrambot.bot.util.KeyboardUtil;
import me.timur.servicesearchtelegrambot.bot.util.PhoneUtil;
import me.timur.servicesearchtelegrambot.enitity.Query;
import me.timur.servicesearchtelegrambot.enitity.Service;
import me.timur.servicesearchtelegrambot.enitity.User;
import me.timur.servicesearchtelegrambot.model.dto.UserDTO;
import me.timur.servicesearchtelegrambot.service.ChatLogService;
import me.timur.servicesearchtelegrambot.service.QueryService;
import me.timur.servicesearchtelegrambot.service.ServiceManager;
import me.timur.servicesearchtelegrambot.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;
import java.util.stream.Collectors;

import static me.timur.servicesearchtelegrambot.bot.util.UpdateUtil.*;

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
    public SendMessage start(Update update) {
        //save if user doesn't exist
        User client = userService.getOrSave(user(update));
        final String name = Objects.nonNull(client.getFirstname()) ? client.getFirstname()
                : Objects.nonNull(client.getLastname()) ? client.getLastname()
                : Objects.nonNull(client.getUsername()) ? client.getUsername()
                : "друг";
//        final SendMessage sendMessage = logAndMessage(update, String.format("Добро пожаловать, %s. Напишите названия сервиса, который вы ищите", name), Outcome.START);
//        sendMessage.setReplyMarkup(removeKeyboard());
//        return sendMessage;

        return logAndKeyboard(
                update,
                String.format("Добро пожаловать, %s. " + Outcome.REGION_REQUESTED.getText(), name),
                Arrays.stream(Region.values()).map(r -> r.russian).collect(Collectors.toList()),
                2,
                Outcome.REGION_REQUESTED
        );
    }

    @Override
    public SendMessage saveRegionAndRequestService(Update update) {
        Region region = Region.getByRussian(command(update));
        if (region == null) {
            return logAndKeyboard(
                    update,
                    Outcome.REGION_REQUESTED.getText(),
                    Arrays.stream(Region.values()).map(r -> r.russian).collect(Collectors.toList()),
                    2,
                    Outcome.REGION_REQUESTED
            );
        } else {
            //set region for a user
            final User user = userService.getUserByTgId(Long.valueOf(chatId(update)));
            user.setRegion(region);
            userService.save(user);
            //search new service
            return searchNewService(update);
        }
    }

    @Override
    public SendMessage searchNewService(Update update) {
        final SendMessage sendMessage = logAndMessage(update,"Напишите названия сервиса, который вы ищите", Outcome.NEW_SEARCH);
        sendMessage.setReplyMarkup(removeKeyboard());
        return sendMessage;
    }

    @Override
    public SendMessage saveQueryIfServiceFoundOrSearchFurther(Update update) {
        Service service = serviceManager.getServiceByName(command(update));
        if (service == null) {
            return searchWithOptions(update);
        } else {
            //save query
            User client = userService.getOrSave(user(update));
            Query query = new Query(client, service);
            queryService.save(query);

            //request query comment
            List<String> keyboard = new ArrayList<>();
            keyboard.add(Outcome.SKIP.getText());
            return logAndKeyboard(
                    update,
                    Outcome.QUERY_COMMENT_REQUESTED.getText(),
                    keyboard,
                    1,
                    Outcome.QUERY_COMMENT_REQUESTED
            );
        }
    }

    @Override
    public List<SendMessage> addQueryCommentAndRequestContact(Update update) {
        //get last active query
        final Query query = queryService.getLastActiveByClientTgId(Long.valueOf(chatId(update))).orElse(null);
        if (query == null) {
            return null;
        }

        // add comment to the query
        final String newCommand = command(update);
        if (!Objects.equals(newCommand, Outcome.SKIP.getText())) {
            query.setComment(newCommand);
            queryService.save(query);
        }

        return requestContact(update);
    }

    private List<SendMessage> requestContact(Update update) {
        User client = userService.getOrSave(user(update));
        List<SendMessage> messages = new ArrayList<>();

        //if client has a username continue with sending notifications. Else request username or phone
        if (client.getUsername() != null) {
            messages.addAll(sendNewQueryNotifications(update));
        } else {
            List<String> keyboard = new ArrayList<>(2);
            keyboard.add("\uD83D\uDC64" + "_" + Outcome.USERNAME.getText());
            keyboard.add("\uD83D\uDCDE" + "_" + Outcome.PHONE.getText());
            // request contact
            SendMessage contactRequest = logAndKeyboard(
                    update,
                    "Пожалуйста, выберите способ обратной связи\n" +
                            "Чтобы с вами могли связаться через имя пользовательское имя в телеграмме, пожалуйста, " +
                            "сначала пропищите её в настройках телеграмма (Настройки -> имя пользователя) " +
                            "и нажмите кнопку `" + Outcome.USERNAME + "`",
                    keyboard,
                    2,
                    Outcome.CONTACT_REQUESTED
            );
            messages.add(contactRequest);
        }

        return messages;
    }

    @Override
    public List<SendMessage> sendNotificationIfUsernamePresent(Update update) {
        List<SendMessage> messages = new ArrayList<>();

        final String userName = update.getMessage().getFrom().getUserName();
        if (userName == null) {
            List<String> keyboard = new ArrayList<>(2);
            keyboard.add("\uD83D\uDC64" + "_" + Outcome.USERNAME.getText());
            keyboard.add("\uD83D\uDCDE" + "_" + Outcome.PHONE.getText());
        // request contact
        SendMessage contactRequest = logAndKeyboard(
              update,
              "Ваше пользовательское имя все ещё пустой. Возможно вы его не прописали"
                  + "Пожалуйста, добавьте его настройках телеграмма (Настройки -> имя пользователя) "
                  + "и потом нажмите кнопку `" + Outcome.USERNAME + "`."
                  + "Если предпочитаете связь через телефон, нажмите кнопку `" + Outcome.PHONE + "`.",
              keyboard,
              2,
              Outcome.CONTACT_REQUESTED);
            messages.add(contactRequest);
        }
        return messages;
    }

    @Override
    public SendMessage requestPhone(Update update) {
        SendMessage phoneRequest = logAndMessage(update, "Пожалуйста, напишите номер телефона, чтобы с вами могли связаться", Outcome.PHONE_REQUESTED);
        phoneRequest.setReplyMarkup(KeyboardUtil.phoneRequest());
        return phoneRequest;
    }

    @Override
    public List<SendMessage> savePhone(Update update) {
        //get and validate phone number
        String phone = update.getMessage().getContact().getPhoneNumber();

        if (!PhoneUtil.isValid(phone)) {
            List<SendMessage> messages = new ArrayList<>();
            SendMessage phoneRequest = logAndMessage(update, Outcome.INVALID_PHONE_FORMAT_SENT.getText(), Outcome.INVALID_PHONE_FORMAT_SENT);
            phoneRequest.setReplyMarkup(KeyboardUtil.phoneRequest());
            messages.add(phoneRequest);
            return messages;
        }

        //set phone to user
        User user = userService.getUserByTgId(user(update).getTelegramId());
        user.setPhone(phone);
        userService.save(user);

        // after getting phone query notifications can be sent
        return sendNewQueryNotifications(update);
    }

    @Override
    public List<SendMessage> sendNewQueryNotifications(Update update) {
        List<SendMessage> messages = new ArrayList<>();

        // prepare messages for providers
        Optional<Query> queryOpt = queryService.getLastActiveByClientTgId(Long.valueOf(chatId(update)));
        if (!queryOpt.isPresent())
            return messages;

        final Query query = queryOpt.get();
        messages.add(providerNotifier.sendToTheGroup(query));

        // prepare message for client
        SendMessage clientMsg = logAndMessage(
                update,
                Outcome.QUERY_SAVED.getText() + ". Номер заявки " + query.getId(),
                Outcome.QUERY_NOTIFIED);

        clientMsg.setReplyMarkup(removeKeyboard());
        messages.add(clientMsg);
        return messages;
    }

    @Override
    public SendMessage searchWithOptions(Update update) {
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
        final List<Query> queries = queryService.getAllActiveByClientTgId(userDTO.getTelegramId());

        if (queries.isEmpty()) {
            final SendMessage sendMessage = logAndMessage(update, "У вас нет активных запросов", Outcome.MY_QUERIES);
            sendMessage.setReplyMarkup(removeKeyboard());
            return sendMessage;
        } else {
            List<String> queryNames = queries.stream()
                    .map(q -> "#" + q.getId() +" : " + q.getService().getName())
                    .collect(Collectors.toList());
            return logAndKeyboard(update, Outcome.MY_QUERIES.getText(), queryNames, 1, Outcome.MY_QUERIES);
        }
    }

    @Override
    public SendMessage getQueryById(Update update) {
        String command = command(update);
        String queryId = command.substring(0, command.indexOf(" "));

        List<String> buttonList = new ArrayList<>();
        buttonList.add(Outcome.DEACTIVATE_QUERY.getText() + " " + queryId);
        buttonList.add(Outcome.BACK_TO_MY_QUERIES.getText());
        return logAndKeyboard(update, command, buttonList, 2, Outcome.CHOOSE_QUERY);
    }

    @Override
    public SendMessage deactivateQuery(Update update) {
        String command = command(update);
        Long queryId = Long.valueOf(
                command.substring(command.indexOf("#") + 1, command.length())
        );

        queryService.delete(queryId);

        return logAndMessage(update, Outcome.QUERY_DEACTIVATED.getText(), Outcome.QUERY_DEACTIVATED);
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
        return keyboard(chatId(update), message, serviceNames, 2);
    }
}
