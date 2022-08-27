package me.timur.servicesearchtelegrambot.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.enitity.ServiceCategory;
import me.timur.servicesearchtelegrambot.model.Lang;

/**
 * Created by Temurbek Ismoilov on 02/05/22.
 */

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceCategoryDTO extends BaseDTO {
    private Long id;
    private String name;
    @JsonProperty("is_active")
    private Boolean isActive;

    public ServiceCategoryDTO(ServiceCategory entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.isActive = entity.isActive();
    }
}
