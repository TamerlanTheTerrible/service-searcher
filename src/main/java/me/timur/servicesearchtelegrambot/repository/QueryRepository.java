package me.timur.servicesearchtelegrambot.repository;

import me.timur.servicesearchtelegrambot.bot.Region;
import me.timur.servicesearchtelegrambot.enitity.Query;
import me.timur.servicesearchtelegrambot.enitity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QueryRepository extends JpaRepository<Query, Long> {
    List<Query> findAllByClientTelegramIdAndIsActiveTrue(Long telegramId);
    Optional<Query> findTopByClientTelegramIdAndIsActiveTrueOrderByIdDesc(Long telegramId);

    List<Query> findAllByServiceInAndClientRegionAndIsActiveTrue(List<Service> services, Region region);
}