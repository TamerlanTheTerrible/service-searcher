package me.timur.servicesearchtelegrambot.service;

import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.enitity.User;
import me.timur.servicesearchtelegrambot.exception.ResourceNotFoundException;
import me.timur.servicesearchtelegrambot.model.dto.UserDTO;
import me.timur.servicesearchtelegrambot.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.requireNonNullElse;

/**
 * Created by Temurbek Ismoilov on 14/05/22.
 */

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void save(UserDTO dto) {
        userRepository.save(new User(dto));
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(format("Could not find user with id %s", id)));
    }

    @Override
    public User getUserByTgId(Long telegramId) {
        return userRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new ResourceNotFoundException(format("Could not find user with telegramId %s", telegramId)));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(format("Could not find user with username %s", username)));
    }

    @Override
    public User getUserByPhone(String phone) {
        return userRepository.findByPhone(phone)
                .orElseThrow(() -> new ResourceNotFoundException(format("Could not find user with phone %s", phone)));
    }

    @Override
    public List<User> getAllByStatus(Boolean isActive) {
        return userRepository.findAllByIsActive(isActive);
    }

    @Override
    public void update(Long id, UserDTO dto) {
        User user = getUserById(id);
        user.setTelegramId(requireNonNullElse(dto.getTelegramId(), user.getTelegramId()));
        user.setUsername(requireNonNullElse(dto.getUsername(), user.getUsername()));
        user.setPhone(requireNonNullElse(dto.getUsername(), user.getUsername()));
        user.setFirstname(requireNonNullElse(dto.getFirstname(), user.getFirstname()));
        user.setLastname(requireNonNullElse(dto.getLastname(), user.getLastname()));
        user.setChatId(requireNonNullElse(dto.getChatId(), user.getChatId()));
        userRepository.save(user);
    }

    @Override
    public void changeStatus(Long id, Boolean status) {
        User user = getUserById(id);
        user.setIsActive(status);
        userRepository.save(user);
    }
}
