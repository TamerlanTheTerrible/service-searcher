package me.timur.servicesearchtelegrambot.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.enitity.ServiceProvider;
import me.timur.servicesearchtelegrambot.util.DateUtil;

import java.sql.Timestamp;

/**
 * Created by Temurbek Ismoilov on 02/05/22.
 */

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceProviderDTO extends BaseDTO {
    private Long id;
    @JsonProperty("date_created")
    @JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN) @JsonDeserialize(using = DateDeserializers.TimestampDeserializer.class)
    private Timestamp dateCreated;
    private UserDTO user;
    private ServiceDTO service;
    @JsonProperty("is_active")
    private Boolean isActive;

    public ServiceProviderDTO(ServiceProvider serviceProvider) {
        this.id = serviceProvider.getId();
        this.user = new UserDTO(serviceProvider.getUser());
        this.dateCreated = serviceProvider.getDateCreated();
        this.service = new ServiceDTO(serviceProvider.getService());
        this.isActive = serviceProvider.getIsActive();
    }
}
