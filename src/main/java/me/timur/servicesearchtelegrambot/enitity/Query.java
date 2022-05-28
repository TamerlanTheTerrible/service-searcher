package me.timur.servicesearchtelegrambot.enitity;

import lombok.*;
import me.timur.servicesearchtelegrambot.model.dto.QueryDTO;

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
    private User client = new User();

    @OneToOne
    @JoinColumn(name = "provider_id")
    private ServiceProvider provider = new ServiceProvider();

    @OneToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service = new Service();

    @ManyToOne
    @JoinColumn(name = "status")
    private QueryStatus status;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public Query(QueryDTO dto) {
        this.client.setId(dto.getClient().getId());
        this.provider.setId(dto.getProvider().getId());
        this.service.setId(dto.getService().getId());
    }

    public Query(User client, ServiceProvider provider, Service service) {
        this.client = client;
        this.provider = provider;
        this.service = service;
    }
}
