package me.timur.servicesearchtelegrambot.repository;

import me.timur.servicesearchtelegrambot.bot.ConfigName;
import me.timur.servicesearchtelegrambot.enitity.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfigRepository extends JpaRepository<Config, Long> {
    Optional<Config> findByName(ConfigName name);
}