package me.timur.servicesearchtelegrambot.service.impl;

import com.pengrad.telegrambot.model.Chat;
import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.enitity.ChatLog;
import me.timur.servicesearchtelegrambot.repository.ChatLogRepository;
import me.timur.servicesearchtelegrambot.service.ChatLogService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by Temurbek Ismoilov on 06/08/22.
 */

@Service
@RequiredArgsConstructor
public class ChatLogServiceImpl implements ChatLogService {

    private final ChatLogRepository chatLogRepository;

    @Override
    public void log(Chat chat) {
        final ChatLog chatLog = new ChatLog(chat.id(), chat.description());
        chatLogRepository.save(chatLog);
    }

    @Override
    public String getLastChatCommand(Chat chat) {
        final Optional<ChatLog> chatLogOpt = chatLogRepository.findTopByTgChatIdOrderByIdDesc(chat.id());
        if (chatLogOpt.isEmpty())
            return null;
        else
            return chatLogOpt.get().getMessage();
    }
}
