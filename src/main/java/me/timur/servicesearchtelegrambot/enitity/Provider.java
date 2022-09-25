package me.timur.servicesearchtelegrambot.enitity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * Created by Temurbek Ismoilov on 02/05/22.
 */

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "provider")
public class Provider extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "provider", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<ProviderService> services;

    public Provider(User user) {
        this.user = user;
        this.isActive = true;
    }

    public String getName() {
        if (this.user == null) return "";
        return Objects.nonNull(this.user.getFirstname()) ? this.user.getFirstname()
                : Objects.nonNull(this.user.getLastname()) ? this.user.getLastname()
                : Objects.nonNull(this.user.getUsername()) ? this.user.getUsername()
                : "друг";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Provider provider = (Provider) o;
        return getId() != null && Objects.equals(getId(), provider.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
