package me.timur.servicesearchtelegrambot.enitity;

import lombok.*;
import me.timur.servicesearchtelegrambot.model.dto.QueryDTO;

import javax.persistence.*;

/**
 * Created by Temurbek Ismoilov on 25/04/22.
 */

@Getter @Setter @RequiredArgsConstructor
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
    @JoinColumn(name = "status")
    private QueryStatus status;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public Query(User client, ServiceProvider provider, Service service) {
        this.client = client;
        this.provider = provider;
        this.service = service;
    }

    public Query(User client, Service service) {
        this.client = client;
        this.service = service;
    }
}
