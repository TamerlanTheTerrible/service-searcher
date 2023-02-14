package me.timur.servicesearchtelegrambot.bot.client.impl;

import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.bot.ChatLogType;
import me.timur.servicesearchtelegrambot.bot.ConfigName;
import me.timur.servicesearchtelegrambot.bot.client.ProviderNotifier;
import me.timur.servicesearchtelegrambot.bot.client.UpdateHandler;
import me.timur.servicesearchtelegrambot.bot.client.enums.Command;
import me.timur.servicesearchtelegrambot.bot.client.enums.Outcome;
import me.timur.servicesearchtelegrambot.bot.Region;
import me.timur.servicesearchtelegrambot.bot.util.KeyboardUtil;
import me.timur.servicesearchtelegrambot.bot.util.StringUtil;
import me.timur.servicesearchtelegrambot.enitity.*;
import me.timur.servicesearchtelegrambot.model.dto.UserDTO;
import me.timur.servicesearchtelegrambot.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;
import java.util.stream.Collectors;

import static me.timur.servicesearchtelegrambot.bot.util.UpdateUtil.*;

/**
 * Created by Temurbek Ismoilov on 23/08/22.
 */

@Component
@RequiredArgsConstructor
public class ClientUpdateHandler implements UpdateHandler {

    @Value("${keyboard.size.row}")
    private Integer keyboardRowSize;

    private final ServiceManager serviceManager;
    private final QueryService queryService;
    private final UserService userService;
    private final ChatLogService chatLogService;
    private final ProviderNotifier providerNotifier;
    private final ConfigService configService;

    @Override
    public SendMessage start(Update update) {
        //save if user doesn't exist
        User client = userService.getOrSave(user(update));
        final String name = Objects.nonNull(client.getFirstname()) ? client.getFirstname()
                : Objects.nonNull(client.getLastname()) ? client.getLastname()
                : Objects.nonNull(client.getUsername()) ? client.getUsername()
                : "друг";

        return logAndKeyboard(
                update,
                String.format("Добро пожаловать, %s. ☺️ " + Outcome.REGION_REQUESTED.getText(), name),
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
            savRegion(update, region);
            //search new service
            return searchNewService(update);
        }
    }

    @Override
    public SendMessage saveRegion(Update update) {
        Region region = Region.getByRussian(command(update));
        savRegion(update, region);
        return logAndKeyboard(update, Outcome.REGION_EDITED.getText(), settingsButtons(region), 2, Outcome.REGION_EDITED);
    }

    private void savRegion(Update update, Region region) {
        final User user = userService.getUserByTgId(Long.valueOf(chatId(update)));
        user.setRegion(region);
        userService.save(user);
    }

    @Override
    public SendMessage searchNewService(Update update) {
        return logAndKeyboard(
            update,
            "Напишите названия сервиса, который вы ищите \uD83D\uDD0E\n",
            backButton(),
            2,
            Outcome.NEW_SEARCH);
    }

    @Override
    public SendMessage saveQueryIfServiceFoundOrSearchFurther(Update update) {
        Service service = serviceManager.getServiceByName(command(update));
        if (service == null) {
            return searchWithOptions(update);
        } else {
            //save query
            return saveQueryAndRequestComment(update, service);
        }
    }

    private SendMessage saveQueryAndRequestComment(Update update, Service service) {
        User client = userService.getOrSave(user(update));
        Query query = new Query(client, service);
        queryService.save(query);

        //request query comment
        List<String> keyboard = new ArrayList<>(backButton());
        keyboard.add(0,Outcome.SKIP.getText());
        keyboard.add(1,Outcome.CANCEL.getText());
        return logAndKeyboard(
                update,
                Outcome.QUERY_COMMENT_REQUESTED.getText(),
                keyboard,
                1,
                Outcome.QUERY_COMMENT_REQUESTED
        );
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
        // validate the command
        if (containsBannedWord(newCommand)) {
            return List.of(message(chatId(update), "Запрещенное слово"));
        }
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
            keyboard.add(Outcome.USERNAME.getText());
            keyboard.add(Outcome.PHONE.getText());
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
            keyboard.add(Outcome.USERNAME.getText());
            keyboard.add(Outcome.PHONE.getText());
            // request contact
            SendMessage contactRequest = logAndKeyboard(
              update,
              "Ваше пользовательское имя все ещё пустой. Возможно вы его не прописали "
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

        if (!StringUtil.isValidPhone(phone)) {
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
        if (queryOpt.isEmpty())
            return messages;

        final Query query = queryOpt.get();
        messages.add(providerNotifier.sendToTheGroup(query));

    // prepare message for client
        SendMessage clientMsg =
            logAndKeyboard(
                update,
                Outcome.QUERY_SAVED.getText() + ". Номер заявки " + query.getId() + "\uD83D\uDE0C",
                mainMenuButtons(),
                2,
                Outcome.QUERY_NOTIFIED);

            messages.add(clientMsg);
            return messages;
    }

    @Override
    public SendMessage cancel(Update update) {
        return logAndKeyboard(update, "Отменен \uD83D\uDEAB", mainMenuButtons(), 2, Outcome.CANCEL);
    }

    @Override
    public SendMessage searchWithOptions(Update update) {
        String command = command(update);

        if (StringUtil.isLatin(command)) {
            return keyboard(
                    chatId(update),
                    "Текст должен быть на кириллице. Попробуйте ещё раз",
                    List.of(Outcome.CATEGORIES.getText(), Command.BACK.getText()),
                    2
            );
        }

        SendMessage sendMessage;
        final List<Service> services = serviceManager.getAllServicesByActiveTrueAndNameLike(command);
        if (services.isEmpty()) {
            List<String> keyboardValues = new ArrayList<>();
            keyboardValues.add(Outcome.CATEGORIES.getText());
            keyboardValues.addAll(backButton());
            sendMessage = logAndKeyboard(update, Outcome.SERVICE_SEARCH_NOT_FOUND.getText(), keyboardValues, 2, Outcome.SERVICE_SEARCH_NOT_FOUND);
//            sendMessage.setReplyMarkup(keyboard(keyboardValues,keyboardRowSize));
        } else {
            final List<String> serviceNames = services.stream().map(Service::getNameUz).collect(Collectors.toList());
            serviceNames.add(Outcome. CATEGORIES.getText());
            serviceNames.addAll(backButton());
            sendMessage = logAndKeyboard(update, Outcome.SERVICE_SEARCH_FOUND.getText(),  serviceNames, keyboardRowSize, Outcome.SERVICE_SEARCH_FOUND);
        }

        return sendMessage;
    }

    @Override
    public SendMessage getServicesByCategoryName(Update update) {
        List<String> servicesNames = serviceManager.getServicesNamesByCategoryName(command(update));
        if (servicesNames.size()==1) {
            Service service = serviceManager.getServiceByName(command(update));
            return saveQueryAndRequestComment(update, service);
        } else {
            ArrayList<String> modifiableList = new ArrayList<>(servicesNames);
            modifiableList.add(Outcome.BACK_TO_CATEGORIES.getText());
            modifiableList.addAll(backButton());
            return logAndKeyboard(update, command(update), modifiableList, keyboardRowSize, Outcome.SERVICES);
        }
    }

    @Override
    public SendMessage getCategories(Update update) {
        final List<String> categoryNames = serviceManager.getActiveCategoryNames();
        categoryNames.addAll(backButton());
        return logAndKeyboard(update, Outcome.CATEGORIES.getText(), categoryNames, keyboardRowSize, Outcome.CATEGORIES);
    }

    @Override
    public SendMessage getUserQueries(Update update) {
        final UserDTO userDTO = user(update);
        final List<Query> queries = queryService.getAllActiveByClientTgId(userDTO.getTelegramId());

        if (queries.isEmpty()) {
            return logAndKeyboard(update, "У вас нет активных запросов", backButton(), 2, Outcome.MY_QUERIES);
        } else {
            List<String> queryNames = queries.stream()
                    .map(q -> "#" + q.getId() +" : " + q.getService().getName())
                    .collect(Collectors.toList());
            queryNames.addAll(List.of(Command.MY_QUERIES_CLOSE_ALL.getText(), Command.BACK.getText()));
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

        return logAndKeyboard(update, Outcome.QUERY_DEACTIVATED.getText(), mainMenuButtons(), 2, Outcome.QUERY_DEACTIVATED);
    }

    @Override
    public SendMessage test(Update update) {
        SendMessage message = message(chatId(update), "just test text");
        message.setReplyMarkup(KeyboardUtil.webAppKeyboard());
        return message;
    }

    @Override
    public SendMessage publicOffer(Update update) {
        Config config = configService.getByName(ConfigName.OFFER);
        return logAndKeyboard(update, config.getValue(), backButton(), 2, Outcome.OFFER);
    }

    @Override
    public SendMessage unknownCommand(Update update) {
        return keyboard(chatId(update), Outcome.UNKNOWN_COMMAND.getText(), mainMenuButtons(), 2);
    }

    @Override
    public SendMessage editRegion(Update update) {
        return logAndKeyboard(
                update,
                Outcome.REGION_REQUESTED.getText(),
                Arrays.stream(Region.values()).map(r -> r.russian).collect(Collectors.toList()),
                2,
                Outcome.REGION_EDIT_REQUESTED
        );
    }

    @Override
    public List<SendMessage> back(Update update){
        String lastChatCommand = chatLogService.getLastChatOutcome(update, ChatLogType.CLIENT);
        List<SendMessage> messages = new ArrayList<>();
        if (lastChatCommand == null) {
            messages.add(message(chatId(update), "нету пути назад"));
        } else if (lastChatCommand.equals(Outcome.SERVICES.name())) {
            messages.add(getCategories(update));
        } else if (List.of(Outcome.NEW_SEARCH.name(), Outcome.MY_QUERIES.name(), Outcome.MY_QUERIES.name()).contains(lastChatCommand)) {
            messages.add(mainMenu(update));
        } else if (Objects.equals(lastChatCommand, Outcome.QUERY_COMMENT_REQUESTED.name()) || Objects.equals(lastChatCommand, Outcome.CATEGORIES.name()) ) {
            final ChatLog chatLog = chatLogService.getLastByOutcome(
                    chatId(update),
                    List.of(Outcome.SERVICE_SEARCH_FOUND.name(), Outcome.SERVICE_SEARCH_NOT_FOUND.name()),
                    ChatLogType.CLIENT);
            if (chatLog == null) {
                messages.add(searchNewService(update));
            } else {
                final Message message = update.getMessage();
                message.setText(chatLog.getCommand());
                update.setMessage(message);
                messages.add(searchWithOptions(update));
            }
        } else if (Objects.equals(lastChatCommand, Outcome.SERVICE_SEARCH_FOUND.name()) || Objects.equals(lastChatCommand, Outcome.SERVICE_SEARCH_NOT_FOUND.name())) {
            messages.add(searchNewService(update));
        } else if (Objects.equals(lastChatCommand, Outcome.OFFER.name()) || Objects.equals(lastChatCommand, Outcome.REGION_EDIT_REQUESTED.name())) {
            messages.addAll(settingsMenu(update));
        } else if (Objects.equals(lastChatCommand, Outcome.SETTINGS.name())) {
            messages.add(mainMenu(update));
        } else {
            SendMessage msg = keyboard(chatId(update), "нету пути назад", mainMenuButtons(), 2);
            messages.add(msg);
        }

        return messages;
    }

    @Override
    public List<SendMessage> settingsMenu(Update update) {
        User client = userService.getUserByTgId(tgUserId(update));
        final SendMessage msg = logAndKeyboard(update, Outcome.SETTINGS.getText(), settingsButtons(client.getRegion()), 2, Outcome.SETTINGS);
        return List.of(msg);
    }

    @Override
    public SendMessage closeActiveQueries(Update update) {
        queryService.closeAll(chatId(update));
    return logAndKeyboard(update, Outcome.MY_QUERIES_CLOSE_ALL.getText(), mainMenuButtons(), 2, Outcome.MY_QUERIES_CLOSE_ALL);
    }

    private SendMessage mainMenu(Update update) {
        return logAndKeyboard(update, Outcome.MAIN_MENU.getText(), mainMenuButtons(), 2, Outcome.MAIN_MENU);
    }

    private SendMessage logAndMessage(Update update, String message, Outcome outcome) {
        chatLogService.log(update, outcome, ChatLogType.CLIENT);
        return message(chatId(update), message);
    }

    private SendMessage logAndKeyboard(Update update, String message, List<String> serviceNames, Integer keyboardRowSize, Outcome outcome) {
        chatLogService.log(update, outcome, ChatLogType.CLIENT);
        return keyboard(chatId(update), message, serviceNames, 2);
    }

    public boolean containsBannedWord(String command) {
        return configService.containsBannedWord(command);
    }

    private List<String> mainMenuButtons() {
        return List.of(
                Command.NEW_SEARCH_BUTTON.getText(),
                Command.MY_QUERIES_BUTTON.getText(),
                Command.SETTINGS.getText()
        );
    }

    private List<String> settingsButtons(Region region) {
        return List.of(
                Command.OFFER_BUTTON.getText(),
                region == null ? "➕ " + Command.REGION_EDIT_BUTTON.getText() : "✏️ " + "Регион" + ": " + region,
                Command.BACK.getText()
        );
    }

    private List<String> backButton() {
        return List.of(
                Command.BACK.getText()
        );
    }
}
