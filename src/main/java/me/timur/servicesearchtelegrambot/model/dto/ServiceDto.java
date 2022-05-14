package me.timur.servicesearchtelegrambot.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.enitity.Service;
import me.timur.servicesearchtelegrambot.model.Lang;
import me.timur.servicesearchtelegrambot.util.DateUtil;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Temurbek Ismoilov on 02/05/22.
 */

@Data
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceDto implements Serializable {
    private Long id;
    @JsonProperty("date_created")
    @JsonFormat(pattern = DateUtil.DATE_TIME_PATTERN) @JsonDeserialize(using = DateDeserializers.TimestampDeserializer.class)
    private Timestamp dateCreated;
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
        this.dateCreated = entity.getDateCreated();
        this.isActive = entity.getActive();
    }
}