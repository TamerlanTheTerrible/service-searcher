package me.timur.servicesearchtelegrambot.controller;

import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.enitity.User;
import me.timur.servicesearchtelegrambot.model.BaseResponse;
import me.timur.servicesearchtelegrambot.model.dto.NoopDTO;
import me.timur.servicesearchtelegrambot.model.dto.UserDTO;
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
    BaseResponse<UserDTO> getById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return BaseResponse.payload(new UserDTO(user));
    }

    @GetMapping("/telegram_id/{telegramId}")
    BaseResponse<UserDTO> getByTelegramId(@PathVariable Long telegramId) {
        User user = userService.getUserByTgId(telegramId);
        return BaseResponse.payload(new UserDTO(user));
    }

    @GetMapping("/phone/{phone}")
    BaseResponse<UserDTO> getByPhone(@PathVariable String phone) {
        User user = userService.getUserByPhone(phone);
        return BaseResponse.payload(new UserDTO(user));
    }

    @GetMapping("/username/{username}")
    BaseResponse<UserDTO> getByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return BaseResponse.payload(new UserDTO(user));
    }

    @PostMapping("")
    BaseResponse<Long> save(@RequestBody UserDTO dto) {
        Long id = userService.save(dto).getId();
        return BaseResponse.payload(id);
    }

    @PutMapping("/{userId}")
    BaseResponse<NoopDTO> update(@PathVariable Long userId, @RequestBody UserDTO dto) {
        userService.update(userId, dto);
        return BaseResponse.payload();
    }

    @DeleteMapping("/{userId}")
    BaseResponse<NoopDTO> delete(@PathVariable Long userId) {
        userService.changeStatus(userId, false);
        return BaseResponse.payload();
    }
}
