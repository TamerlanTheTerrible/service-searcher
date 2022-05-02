package me.timur.servicesearchtelegrambot.enitity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by Temurbek Ismoilov on 25/04/22.
 */

@Data
@Entity
@Table(name = "service_provider_subscription")
public class ServiceProviderSubscription extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private ServiceProvider provider;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
