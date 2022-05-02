package me.timur.servicesearchtelegrambot.enitity;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by Temurbek Ismoilov on 02/05/22.
 */

@Data
@Entity
@Table(name = "service_provider")
public class ServiceProvider extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User provider;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = false;
}
