package me.timur.servicesearchtelegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.bot.ChatLogType;
import me.timur.servicesearchtelegrambot.enitity.ChatLog;
import me.timur.servicesearchtelegrambot.bot.client.enums.Outcome;
import me.timur.servicesearchtelegrambot.repository.ChatLogRepository;
import me.timur.servicesearchtelegrambot.service.ChatLogService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;

import static me.timur.servicesearchtelegrambot.bot.util.UpdateUtil.*;

/**
 * Created by Temurbek Ismoilov on 06/08/22.
 */

@Service
@RequiredArgsConstructor
public class ChatLogServiceImpl implements ChatLogService {

    private final ChatLogRepository chatLogRepository;

    @Override
    public void log(Update update, Outcome outcome, ChatLogType logType) {
        final ChatLog chatLog = new ChatLog(update, outcome, logType);
        chatLogRepository.save(chatLog);
    }

    @Override
    public void log(Update update, me.timur.servicesearchtelegrambot.bot.provider.enums.Outcome outcome, ChatLogType logType) {
        final ChatLog chatLog = new ChatLog(update, outcome, logType);
        chatLogRepository.save(chatLog);
    }

    @Override
    public String getLastChatOutcome(Update update, ChatLogType type) {
        final Optional<ChatLog> chatLogOpt = chatLogRepository.findTopByTgChatIdAndLogTypeOrderByIdDesc(chatId(update), type);
        return chatLogOpt.map(ChatLog::getOutcome).orElse(null);
    }

    @Override
    public ChatLog getLastByOutcome(String tgChatId, List<String> outcomes, ChatLogType logType) {
            return chatLogRepository.findTopByTgChatIdAndOutcomeInAndLogTypeOrderByIdDesc(tgChatId, outcomes, logType).orElse(null);
    }
}
