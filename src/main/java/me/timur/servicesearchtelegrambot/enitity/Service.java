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
@Table(name = "service")
public class Service extends BaseEntity{

    @Column(name = "name", nullable = false)
    private String name;

    @Convert(converter = LangToJsonConverter.class)
    @Column(name = "lang", nullable = false)
    private Lang lang;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private ServiceCategory category;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
