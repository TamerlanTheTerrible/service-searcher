package me.timur.servicesearchtelegrambot.enitity;

import lombok.*;
import me.timur.servicesearchtelegrambot.model.dto.QueryDto;

import javax.persistence.*;

/**
 * Created by Temurbek Ismoilov on 25/04/22.
 */

@Data @RequiredArgsConstructor
@Entity
@Table(name = "query")
public class Query extends BaseEntity{

    @OneToOne
    @JoinColumn(name = "client_user_id", nullable = false)
    private User client;

    @OneToOne
    @JoinColumn(name = "provider_id")
    private ServiceProvider provider;

    @OneToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @ManyToOne
    @JoinColumn(name = "status", nullable = false)
    private QueryStatus status;

    public Query(QueryDto dto) {
        this.client.setId(dto.getClient().getId());
        this.provider.setId(dto.getProvider().getId());
        this.service.setId(dto.getService().getId());
    }
}
