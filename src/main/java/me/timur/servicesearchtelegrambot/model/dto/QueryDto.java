package me.timur.servicesearchtelegrambot.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import me.timur.servicesearchtelegrambot.util.DateUtil;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by Temurbek Ismoilov on 02/05/22.
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryDto implements Serializable {
    private Long id;
//
//    @JsonProperty("date_created") @JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class) @JsonSerialize(using = LocalDateTimeSerializer.class)
//    private LocalDateTime dateCreated;

    private UserDto client;
    private ServiceProviderDto provider;
    private ServiceDto service;
    private QueryStatusDto status;
}
