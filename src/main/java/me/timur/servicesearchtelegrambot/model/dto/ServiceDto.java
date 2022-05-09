package me.timur.servicesearchtelegrambot.model.dto;

import lombok.Data;
import me.timur.servicesearchtelegrambot.model.Lang;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Temurbek Ismoilov on 02/05/22.
 */

@Data
public class ServiceDto implements Serializable {
    private Long id;
    private String dateCreated;
    private String name;
    private Lang lang;
    private ServiceCategoryDto category;
    private Boolean isActive;
}
