package me.timur.servicesearchtelegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.bot.ConfigName;
import me.timur.servicesearchtelegrambot.enitity.Config;
import me.timur.servicesearchtelegrambot.repository.ConfigRepository;
import me.timur.servicesearchtelegrambot.service.ConfigService;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by Temurbek Ismoilov on 01/02/23.
 */

@Service
@RequiredArgsConstructor
public class ConfigServiceImpl implements ConfigService {
    private final ConfigRepository configRepository;

    @Override
    public Config getByName(ConfigName name) {
        return configRepository.findByName(name)
                .orElse(new Config(ConfigName.OFFER, "публичная оферта"));
    }

    @Override
    public boolean containsBannedWord(String command) {
        String[] commandWords = command.toLowerCase(Locale.ROOT).split(" ");
        Optional<Config> configOpt = configRepository.findByName(ConfigName.BANNED_WORDS);
        if (configOpt.isEmpty()) {
            return false;
        }

        Config config = configOpt.get();
        final String[] bannedWords = config.getValue().split(" ");
        for (String word: commandWords) {
            for (String bannedWord: bannedWords) {
                if (Objects.equals(word, bannedWord)) {
                    return true;
                }
            }
        }
        return false;
    }
}
