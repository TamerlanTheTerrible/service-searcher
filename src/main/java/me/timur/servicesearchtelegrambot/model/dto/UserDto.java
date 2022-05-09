package me.timur.servicesearchtelegrambot.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Temurbek Ismoilov on 02/05/22.
 */

@Data
public class UserDto implements Serializable {
    private Long id;
    private String dateCreated;
    private Long telegramId;
    private String userName;
    private String lastName;
    private String firstName;
    private String phone;
    private String chatId;
    private Boolean isActive;
}
