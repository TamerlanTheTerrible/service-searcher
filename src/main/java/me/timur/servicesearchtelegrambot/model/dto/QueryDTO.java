package me.timur.servicesearchtelegrambot.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by Temurbek Ismoilov on 02/05/22.
 */

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryDTO extends BaseDTO  {
    private Long id;
//
//    @JsonProperty("date_created") @JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN)
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class) @JsonSerialize(using = LocalDateTimeSerializer.class)
//    private LocalDateTime dateCreated;

    private UserDTO client;
    private ServiceProviderDTO provider;
    private ServiceDTO service;
    private QueryStatusDTO status;
}
