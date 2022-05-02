package me.timur.servicesearchtelegrambot.enitity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by Temurbek Ismoilov on 25/04/22.
 */

@Data
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
}
