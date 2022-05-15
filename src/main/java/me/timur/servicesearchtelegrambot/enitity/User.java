package me.timur.servicesearchtelegrambot.enitity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.model.dto.UserDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Temurbek Ismoilov on 25/04/22.
 */

@Data @RequiredArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(name = "telegram_id", nullable = false, unique = true)
    private Long telegramId;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "last_name")
    private String lastname;

    @Column(name = "first_name")
    private String firstname;

    @Column(name = "phone", unique = true)
    private String phone;

    @Column(name = "chat_id", nullable = false)
    private String chatId;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public User(UserDTO dto) {
        this.telegramId = dto.getTelegramId();
        this.username = dto.getUsername();
        this.firstname = dto.getFirstname();
        this.lastname = dto.getLastname();
        this.phone = dto.getPhone().replace(" ", "").replace("+", "");
        this.chatId = dto.getChatId();
        this.isActive = true;
    }
}
