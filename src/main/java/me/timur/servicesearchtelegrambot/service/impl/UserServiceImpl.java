package me.timur.servicesearchtelegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.enitity.User;
import me.timur.servicesearchtelegrambot.exception.ResourceNotFoundException;
import me.timur.servicesearchtelegrambot.model.dto.UserDTO;
import me.timur.servicesearchtelegrambot.repository.UserRepository;
import me.timur.servicesearchtelegrambot.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

/**
 * Created by Temurbek Ismoilov on 14/05/22.
 */

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User save(UserDTO dto) {
        return userRepository.save(new User(dto));
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(format("Could not find user with id %s", id)));
    }

    @Override
    public User getActiveUserById(Long id) {
        return userRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException(format("Could not find user with id %s", id)));
    }

    @Override
    public User getUserByTgId(Long telegramId) {
        return userRepository.findByTelegramId(telegramId)
                .orElse(null);
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
        user.setTelegramId(dto.getTelegramId() != null ? dto.getTelegramId() : user.getTelegramId());
        user.setUsername(dto.getUsername() != null ? dto.getUsername() : user.getUsername());
        user.setPhone(dto.getUsername() != null ? dto.getUsername() : user.getUsername());
        user.setFirstname(dto.getFirstname() != null ? dto.getFirstname() : user.getFirstname());
        user.setLastname(dto.getLastname() != dto.getLastname() ? dto.getLastname() : user.getLastname());
        userRepository.save(user);
    }

    @Override
    public void changeStatus(Long id, Boolean status) {
        User user = getUserById(id);
        user.setIsActive(status);
        userRepository.save(user);
    }

    @Override
    public User getOrSave(UserDTO dto) {
        User user = getUserByTgId(dto.getTelegramId());
        return Objects.nonNull(user)
                ? user
                : save(dto);
    }
}
