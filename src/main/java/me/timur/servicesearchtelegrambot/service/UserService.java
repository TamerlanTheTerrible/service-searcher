package me.timur.servicesearchtelegrambot.service;

import me.timur.servicesearchtelegrambot.enitity.User;
import me.timur.servicesearchtelegrambot.model.dto.UserDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Temurbek Ismoilov on 14/05/22.
 */

@Transactional
public interface UserService {

    void save(UserDto dto);

    User getUserById(Long id);

    User getUserByTgId(Long telegramId);

    User getUserByUsername(String username);

    User getUserByPhone(String phone);

    List<User> getAllByStatus(Boolean isActive);

    void update(Long id, UserDto dto);

    void changeStatus(Long id, Boolean status);
}
