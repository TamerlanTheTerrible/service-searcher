package me.timur.servicesearchtelegrambot.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.enitity.Service;
import me.timur.servicesearchtelegrambot.model.Lang;

import java.io.Serializable;

/**
 * Created by Temurbek Ismoilov on 02/05/22.
 */

@Data
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceDto implements Serializable {
    private Long id;
    @JsonProperty("date_created")
    private String dateCreated;
    private String name;
    private Lang lang;
    private ServiceCategoryDto category;
    @JsonProperty("is_active")
    private Boolean isActive;

    public ServiceDto(Service entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.lang = entity.getLang();
        this.category = new ServiceCategoryDto(entity.getCategory());
        this.isActive = entity.getActive();
    }
}