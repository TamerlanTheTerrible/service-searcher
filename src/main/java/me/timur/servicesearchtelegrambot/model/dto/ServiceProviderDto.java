package me.timur.servicesearchtelegrambot.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import lombok.Data;
import me.timur.servicesearchtelegrambot.util.DateUtil;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by Temurbek Ismoilov on 02/05/22.
 */

@Data
public class ServiceProviderDto implements Serializable {
    private Long id;
    @JsonProperty("date_created")
    @JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN) @JsonDeserialize(using = DateDeserializers.TimestampDeserializer.class)
    private LocalDateTime dateCreated;
    private ServiceDto service;
    @JsonProperty("is_active")
    private Boolean isActive;
}
