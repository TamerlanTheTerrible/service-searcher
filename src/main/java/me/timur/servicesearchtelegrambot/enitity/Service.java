package me.timur.servicesearchtelegrambot.enitity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.model.Lang;
import me.timur.servicesearchtelegrambot.model.dto.ServiceDto;
import me.timur.servicesearchtelegrambot.util.LangToJsonConverter;

import javax.persistence.*;

/**
 * Created by Temurbek Ismoilov on 25/04/22.
 */

@Data @RequiredArgsConstructor
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
    private Boolean active = true;

    public Service(ServiceDto dto, ServiceCategory category) {
        this.name = dto.getName().trim().toUpperCase();
        this.lang = dto.getLang();
        this.category = category;
    }
}
