package me.timur.servicesearchtelegrambot.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.enitity.Service;
import me.timur.servicesearchtelegrambot.model.enums.Command;
import me.timur.servicesearchtelegrambot.service.ChatLogService;
import me.timur.servicesearchtelegrambot.service.ServiceManager;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Temurbek Ismoilov on 30/05/22.
 */

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class BotService {

    private final ChatLogService chatLogService;
    private final ServiceManager serviceManager;

    private final static String botName = "UzservicesearchBot";
    private final static String botToken = "5452269303:AAGodrX6ZbfbFfo5GNkOK2CArsmyAqpdeyE";

    public String token() {
        return botToken;
    }

    public SendMessage start(User user, Chat chat) {
        chatLogService.log(chat);
        String responseMsg = "Добро пожаловать\n Напишите название услуги, которую вы ищите";
        return new SendMessage(chat.id(),responseMsg);
    }

    public SendMessage anyText(Chat chat, Update update) {
        if (!isSearchingService(chat))
            return new SendMessage(chat.id(),"Неизвестная команда");

        String serviceName = update.message().text();
        List<Service> services = serviceManager.getAllServicesByNameLike(serviceName);

        if (services.size() == 0)
            return new SendMessage(chat.id(),"Не удалось найти сервис. Пожалуйста попробуйте еще раз");

        String availableServiceNames = services.stream()
                .map(s -> s.getLang().getUz())
                .collect(Collectors.joining("\n"));
        return new SendMessage(chat.id(), availableServiceNames);
    }

    /**
     * the method returns true if
     * (1) this is a first command/message (e.g. first record in the chat)
     * (2) or the previous command/message was "/start"
     */
    private boolean isSearchingService(Chat chat) {
        String lastChatCommand = chatLogService.getLastChatCommand(chat);
        if (lastChatCommand == null)
            return true;
        else
            return Objects.equals(lastChatCommand, Command.START.getStringValue());
    }
}
