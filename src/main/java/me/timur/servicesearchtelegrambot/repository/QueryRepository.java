package me.timur.servicesearchtelegrambot.repository;

import me.timur.servicesearchtelegrambot.enitity.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QueryRepository extends JpaRepository<Query, Long> {
    List<Query> findAllByClientTelegramIdAndIsActiveTrue(Long telegramId);
    Optional<Query> findTopByClientTelegramIdAndIsActiveTrueOrderByIdDesc(Long telegramId);
}