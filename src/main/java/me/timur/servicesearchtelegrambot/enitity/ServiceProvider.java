package me.timur.servicesearchtelegrambot.enitity;

import lombok.*;
import me.timur.servicesearchtelegrambot.model.dto.ServiceProviderDTO;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

/**
 * Created by Temurbek Ismoilov on 02/05/22.
 */

@Getter
@Setter
@ToString
@RequiredArgsConstructor
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ServiceProvider provider = (ServiceProvider) o;
        return getId() != null && Objects.equals(getId(), provider.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
