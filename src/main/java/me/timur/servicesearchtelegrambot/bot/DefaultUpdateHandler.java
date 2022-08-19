package me.timur.servicesearchtelegrambot.bot;

import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.enitity.Service;
import me.timur.servicesearchtelegrambot.model.enums.Command;
import me.timur.servicesearchtelegrambot.model.enums.Outcome;
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
        return replyList;
    }

    private SendMessage logAndMessage(Update update, String message, Outcome outcome) {
        chatLogService.log(update, outcome);
        return message(chatId(update), message);
    }

    private SendMessage logAndKeyboard(Update update, List<String> serviceNames, Integer keyboardRowSize, Outcome outcome) {
        chatLogService.log(update, outcome);
        return keyboard(chatId(update), serviceNames, keyboardRowSize);
    }

    private SendMessage start(Update update) {
        return logAndMessage(update,"Добро пожаловать. Напишите названия сервиса, который вы ищите", Outcome.START);
    }

    private SendMessage searchService(Update update) {
        String command = command(update);

        SendMessage sendMessage;
        final String lastChatCommand = chatLogService.getLastChatOutcome(update);
        if (lastChatCommand == null || lastChatCommand.equals(Outcome.START.name()) || lastChatCommand.equals(Outcome.SERVICE_SEARCH_FAILED.name())) {
            final List<Service> services = serviceManager.getAllServicesByActiveTrueAndNameLike(command);
            if (services.isEmpty()) {
                sendMessage = logAndMessage(update, "Не удалось найти сервис. Попробуйте еще раз или выберите из списка", Outcome.SERVICE_SEARCH_FAILED);
            } else {
                final List<String> serviceNames = services.stream().map(s -> s.getLang().getUz()).toList();
                sendMessage = logAndKeyboard(update, serviceNames, keyboardRowSize, Outcome.SERVICE_SEARCH_SUCCESS);
            }
        }
        else {
            sendMessage = logAndMessage(update, "Неизвестная команда. Попробуйте еще раз", Outcome.UNKNOWN_COMMAND);
        }

        return sendMessage;
    }



}
