package me.timur.servicesearchtelegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.enitity.ServiceProvider;
import me.timur.servicesearchtelegrambot.exception.ResourceNotFoundException;
import me.timur.servicesearchtelegrambot.model.dto.ServiceProviderDTO;
import me.timur.servicesearchtelegrambot.repository.ServiceProviderRepository;
import me.timur.servicesearchtelegrambot.service.ServiceManager;
import me.timur.servicesearchtelegrambot.service.ServiceProviderService;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

/**
 * Created by Temurbek Ismoilov on 16/05/22.
 */

@Service
@RequiredArgsConstructor
public class ServiceProviderServiceImpl implements ServiceProviderService {

    private final ServiceProviderRepository providerRepository;
    private final ServiceManager serviceManager;

    @Override
    public ServiceProvider getById(Long id) {
        return providerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(format("Could not find service with id %s", id)));
    }

    @Override
    public ServiceProvider getActiveById(Long id) {
        return providerRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException(format("Could not find service with id %s", id)));
    }

    @Override
    public Long save(ServiceProviderDTO dto) {
        final ServiceProvider provider = providerRepository.save(new ServiceProvider(dto));
        return provider.getId();
    }

    @Override
    public List<ServiceProvider> getAll() {
        return providerRepository.findAll();
    }

    @Override
    public void update(Long providerId, ServiceProviderDTO dto) {
        ServiceProvider provider = getById(providerId);

        final var service = serviceManager.getService(dto.getService().getId());
        provider.setService(service);

        providerRepository.save(provider);
    }

    @Override
    public void delete(Long providerId) {
        ServiceProvider provider = getById(providerId);
        provider.setIsActive(false);
        providerRepository.save(provider);
    }
}
