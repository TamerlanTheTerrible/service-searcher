package me.timur.servicesearchtelegrambot.enitity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.model.Lang;
import me.timur.servicesearchtelegrambot.model.dto.ServiceDTO;
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

    @Column(name = "name_uz", nullable = false)
    private String nameUz;

    @Column(name = "name_ru", nullable = false)
    private String nameRu;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private ServiceCategory category;

    @Column(name = "is_active", nullable = false)
    private Boolean active = true;

    public Service(ServiceDTO dto, ServiceCategory category) {
        this.name = dto.getName().trim().toUpperCase();
        this.nameUz = dto.getNameUz();
        this.nameRu = dto.getNameRu();
        this.category = category;
    }
}
