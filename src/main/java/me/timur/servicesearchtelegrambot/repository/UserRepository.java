package me.timur.servicesearchtelegrambot.repository;

import me.timur.servicesearchtelegrambot.enitity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByTelegramId(Long telegramId);

    Optional<User> findByPhone(String phone);

    List<User> findAllByIsActive(Boolean isActive);

}