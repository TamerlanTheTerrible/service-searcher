package me.timur.servicesearchtelegrambot.enitity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * Created by Temurbek Ismoilov on 25/09/22.
 */

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "provider_service")
public class ProviderService extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private Provider provider;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;
}
