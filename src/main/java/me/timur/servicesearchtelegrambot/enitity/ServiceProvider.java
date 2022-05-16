package me.timur.servicesearchtelegrambot.enitity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.model.dto.ServiceProviderDTO;

import javax.persistence.*;

/**
 * Created by Temurbek Ismoilov on 02/05/22.
 */

@Data @RequiredArgsConstructor
@Entity
@Table(name = "service_provider")
public class ServiceProvider extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user = new User();

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service = new Service();

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public ServiceProvider(ServiceProviderDTO dto) {
        this.user.setId(dto.getUser().getId());
        this.service.setId(dto.getService().getId());
        this.isActive = true;
    }
}
