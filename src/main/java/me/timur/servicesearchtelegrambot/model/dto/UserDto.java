package me.timur.servicesearchtelegrambot.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.enitity.User;
import me.timur.servicesearchtelegrambot.util.DateUtil;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by Temurbek Ismoilov on 02/05/22.
 */

@Data
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto implements Serializable {
    private Long id;

    @JsonProperty("date_created")
    @JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN) @JsonDeserialize(using = DateDeserializers.TimestampDeserializer.class)
    private Timestamp dateCreated;

    @JsonProperty("telegram_id")
    private Long telegramId;
    private String username;
    private String lastname;
    private String firstname;
    private String phone;
    @JsonProperty("chat_id")
    private String chatId;
    @JsonProperty("is_active")
    private Boolean isActive;

    public UserDto(User user) {
        this.id = user.getId();
        this.dateCreated = user.getDateCreated();
        this.telegramId = user.getTelegramId();
        this.username = user.getUsername();
        this.lastname = user.getLastname();
        this.firstname = user.getFirstname();
        this.phone = user.getPhone();
        this.chatId = user.getChatId();
        this.isActive = user.getIsActive();
    }
}
