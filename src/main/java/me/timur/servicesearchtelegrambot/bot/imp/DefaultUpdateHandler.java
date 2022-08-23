package me.timur.servicesearchtelegrambot.bot.imp;

import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.bot.UpdateHandler;
import me.timur.servicesearchtelegrambot.enitity.Query;
import me.timur.servicesearchtelegrambot.enitity.Service;
import me.timur.servicesearchtelegrambot.enitity.User;
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

import java.util.List;
import java.util.Objects;

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

    public SendMessage saveQueryIfServiceFoundOrSearchFurther(Update update) {
        Service service = serviceManager.getServiceByName(command(update));
        SendMessage sendMessage;
        if (service == null) {
            sendMessage = searchService(update);
        } else {
            User client = userService.getOrSave(user(update));
            Query query = new Query(client, service);
            queryService.save(query);
            //TODO find and notify service providers
            sendMessage = logAndMessage(update, "Ваша заявка принято. С вами свяжутся, как только найдется нужный сервис провайдер", Outcome.QUERY_SAVED);
        }
        return sendMessage;
    }

    public SendMessage searchService(Update update) {
        String command = command(update);
        SendMessage sendMessage;

        final List<Service> services = serviceManager.getAllServicesByActiveTrueAndNameLike(command);
        if (services.isEmpty()) {
            sendMessage = logAndMessage(update, "Не удалось найти сервис. Попробуйте еще раз или выберите из списка", Outcome.SERVICE_SEARCH_FAILED);
        } else {
            final List<String> serviceNames = services.stream().map(Service::getNameUz).toList();
            sendMessage = logAndKeyboard(update, serviceNames, keyboardRowSize, Outcome.SERVICE_SEARCH_SUCCESS);
        }

        return sendMessage;
    }

    public SendMessage start(Update update) {
        //save if user doesn't exist
        User client = userService.getOrSave(user(update));
        final String name = Objects.nonNull(client.getFirstname()) ? client.getFirstname()
                : Objects.nonNull(client.getLastname()) ? client.getLastname()
                : Objects.nonNull(client.getUsername()) ? client.getUsername()
                : "друг";
        return logAndMessage(update,String.format("Добро пожаловать, %s. Напишите названия сервиса, который вы ищите", name), Outcome.START);
    }

    public SendMessage unknownCommand(Update update) {
        return logAndMessage(update, "Неизвестная команда. Попробуйте еще раз", Outcome.UNKNOWN_COMMAND);
    }

    private SendMessage logAndMessage(Update update, String message, Outcome outcome) {
        chatLogService.log(update, outcome);
        return message(chatId(update), message);
    }

    private SendMessage logAndKeyboard(Update update, List<String> serviceNames, Integer keyboardRowSize, Outcome outcome) {
        chatLogService.log(update, outcome);
        return keyboard(chatId(update), serviceNames, keyboardRowSize);
    }
}
