package me.timur.servicesearchtelegrambot.enitity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Temurbek Ismoilov on 25/04/22.
 */

@Data
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(name = "telegram_id", nullable = false)
    private Long telegramId;

    @Column(name = "username", nullable = false)
    private String userName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "chat_id", nullable = false)
    private String chatId;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
