package me.timur.servicesearchtelegrambot.bot;

import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.enitity.Service;
import me.timur.servicesearchtelegrambot.model.enums.Command;
import me.timur.servicesearchtelegrambot.service.ChatLogService;
import me.timur.servicesearchtelegrambot.service.ServiceManager;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    private final static Integer KEYBOARD_COLUMN_SIZE = 2;

    public String token() {
        return botToken;
    }

    public SendMessage start(Update update, Chat chat) {
        String responseMsg = "Добро пожаловать\nНапишите название услуги, которую вы ищите";
        return new SendMessage(chat.getId().toString(),responseMsg);
    }

    public ReplyKeyboardMarkup anyText(Chat chat, Update update) {
//        if (!isSearchingService(chat))
//            return (T) new SendMessage(chat.id(),"Неизвестная команда");

        String serviceName = update.getMessage().getText();
        List<Service> services = serviceManager.getAllServicesByActiveTrueAndNameLike(serviceName);

//        if (services.size() == 0)
//            return (T) new SendMessage(chat.id(),"Не удалось найти сервис. Пожалуйста попробуйте еще раз");

        List<String> serviceNameList = new ArrayList<>(services.stream().map(s -> s.getLang().getUz()).toList());

        final int rowSize = (int) Math.ceil((double)serviceNameList.size() / KEYBOARD_COLUMN_SIZE);
        String[][] keyboardWords = new String[rowSize][KEYBOARD_COLUMN_SIZE];

        for (int r = 0; r < rowSize; r++) {
            for (int c = 0; c < KEYBOARD_COLUMN_SIZE; c++) {
                if (!serviceNameList.isEmpty()) {
                    keyboardWords[r][c] = serviceNameList.get(0);
                    serviceNameList.remove(0);
                }
            }
        }

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        return new ReplyKeyboardMarkup();
    }

    /**
     * the method returns true if
     * (1) this is a first command/message (e.g. first record in the chat)
     * (2) or the previous command/message was "/start"
     */
    private boolean isSearchingService(Update update) {
        String lastChatCommand = chatLogService.getLastChatOutcome(update);
        if (lastChatCommand == null)
            return true;
        else
            return Objects.equals(lastChatCommand, Command.START.getValue());
    }
}
