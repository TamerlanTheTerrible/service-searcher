package me.timur.servicesearchtelegrambot.bot;

import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.enitity.Service;
import me.timur.servicesearchtelegrambot.model.enums.Command;
import me.timur.servicesearchtelegrambot.service.ChatLogService;
import me.timur.servicesearchtelegrambot.service.ServiceManager;
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
public class DefaultUpdateHandler implements UpdateHandler {

    @Value("${keyboard.size.row}")
    private Integer keyboardRowSize;

    private final ChatLogService chatLogService;
    private final ServiceManager serviceManager;

    @Override
    public List<BotApiMethod<Message>> handle(Update update) {
        String command = command(update);

        List<BotApiMethod<Message>> replyList = new ArrayList<>();
        SendMessage sendMessage;

        if (Objects.equals(command, Command.START.getValue()))
            sendMessage = start(update);
        else
            sendMessage = searchService(update);

        replyList.add(sendMessage);
        chatLogService.log(update);

        return replyList;
    }

    private SendMessage start(Update update) {
        final String chatId = chatId(update);

        return message(chatId, "Добро пожаловать");
    }

    private SendMessage searchService(Update update) {
        final String chatId = chatId(update);
        String command = command(update);
        SendMessage sendMessage;

        final String lastChatCommand = chatLogService.getLastChatCommand(update);
        if (lastChatCommand == null || lastChatCommand.equals(Command.START.getValue())) {
            final List<Service> services = serviceManager.getAllServicesByActiveTrueAndNameLike(command);
            if (services.isEmpty()) {
                sendMessage = message(chatId, "Не удалось найти сервис. Попробуйте еще раз или выберите из списка");
            } else {
                final List<String> serviceNames = services.stream().map(s -> s.getLang().getUz()).toList();
                sendMessage = keyboard(chatId, serviceNames, keyboardRowSize);
            }
        }
        else {
            sendMessage = message(chatId, "Неизвестная команда. Попробуйте еще раз");
        }

        return sendMessage;
    }


}
