package me.timur.servicesearchtelegrambot.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.enitity.ServiceCategory;
import me.timur.servicesearchtelegrambot.model.Lang;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Temurbek Ismoilov on 02/05/22.
 */

@Data
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceCategoryDto implements Serializable {
    private Long id;
    private String name;
    private Lang lang;
    @JsonProperty("is_active")
    private Boolean isActive;

    public ServiceCategoryDto(ServiceCategory entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.lang = entity.getLang();
        this.isActive = entity.isActive();
    }
}
