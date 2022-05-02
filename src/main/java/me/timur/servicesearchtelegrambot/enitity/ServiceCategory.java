package me.timur.servicesearchtelegrambot.enitity;

import lombok.Data;
import me.timur.servicesearchtelegrambot.model.Lang;
import me.timur.servicesearchtelegrambot.util.LangToJsonConverter;

import javax.persistence.*;

/**
 * Created by Temurbek Ismoilov on 25/04/22.
 */

@Data
@Entity
@Table(name = "service_category")
public class ServiceCategory extends BaseEntity{

    @Column(name = "name", nullable = false)
    private String name;

    @Convert(converter = LangToJsonConverter.class)
    @Column(name = "lang", nullable = false)
    private Lang lang;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
