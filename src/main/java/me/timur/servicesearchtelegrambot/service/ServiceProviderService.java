package me.timur.servicesearchtelegrambot.service;

import me.timur.servicesearchtelegrambot.enitity.Service;
import me.timur.servicesearchtelegrambot.enitity.ServiceProvider;
import me.timur.servicesearchtelegrambot.model.dto.ServiceProviderDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Temurbek Ismoilov on 16/05/22.
 */

@Transactional
public interface ServiceProviderService {

    ServiceProvider getById(Long providerId);

    ServiceProvider getActiveById(Long id);

    Long save(ServiceProviderDTO dto);

    List<ServiceProvider> getAll();

    void update(Long providerId, ServiceProviderDTO dto);

    void delete(Long providerId);

    List<ServiceProvider> findAllByService(Service service);
}
