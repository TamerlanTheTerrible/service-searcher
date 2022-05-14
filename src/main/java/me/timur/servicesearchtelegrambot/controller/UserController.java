package me.timur.servicesearchtelegrambot.controller;

import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.enitity.User;
import me.timur.servicesearchtelegrambot.model.BaseResponse;
import me.timur.servicesearchtelegrambot.model.dto.UserDto;
import me.timur.servicesearchtelegrambot.service.UserService;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Temurbek Ismoilov on 14/05/22.
 */

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/id/{userId}")
    BaseResponse getById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return BaseResponse.payload(new UserDto(user));
    }

    @GetMapping("/telegram_id/{telegramId}")
    BaseResponse getByTelegramId(@PathVariable Long telegramId) {
        User user = userService.getUserByTgId(telegramId);
        return BaseResponse.payload(new UserDto(user));
    }

    @GetMapping("/phone/{phone}")
    BaseResponse getByPhone(@PathVariable String phone) {
        User user = userService.getUserByPhone(phone);
        return BaseResponse.payload(new UserDto(user));
    }

    @GetMapping("/username/{username}")
    BaseResponse getByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return BaseResponse.payload(new UserDto(user));
    }

    @PostMapping("/")
    BaseResponse save(@RequestBody UserDto dto) {
        userService.save(dto);
        return BaseResponse.payload(null);
    }

    @PutMapping("/{userId}")
    BaseResponse update(@PathVariable Long userId, @RequestBody UserDto dto) {
        userService.update(userId, dto);
        return BaseResponse.payload(null);
    }

    @DeleteMapping("/{userId}")
    BaseResponse delete(@PathVariable Long userId) {
        userService.changeStatus(userId, false);
        return BaseResponse.payload(null);
    }
}
