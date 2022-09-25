package me.timur.servicesearchtelegrambot.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.enitity.User;
import me.timur.servicesearchtelegrambot.util.DateUtil;

import java.sql.Timestamp;

/**
 * Created by Temurbek Ismoilov on 02/05/22.
 */

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO extends BaseDTO {
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
    @JsonProperty("is_active")
    private Boolean isActive;

    public UserDTO(User user) {
        this.id = user.getId();
        this.dateCreated = user.getDateCreated();
        this.telegramId = user.getTelegramId();
        this.username = user.getUsername();
        this.lastname = user.getLastname();
        this.firstname = user.getFirstname();
        this.phone = user.getPhone();
        this.isActive = user.getIsActive();
    }
}
