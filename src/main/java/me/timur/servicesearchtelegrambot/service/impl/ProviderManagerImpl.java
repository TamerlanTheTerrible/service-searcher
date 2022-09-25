package me.timur.servicesearchtelegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.enitity.Provider;
import me.timur.servicesearchtelegrambot.enitity.User;
import me.timur.servicesearchtelegrambot.exception.ResourceNotFoundException;
import me.timur.servicesearchtelegrambot.model.dto.ServiceProviderDTO;
import me.timur.servicesearchtelegrambot.model.dto.UserDTO;
import me.timur.servicesearchtelegrambot.repository.ProviderRepository;
import me.timur.servicesearchtelegrambot.service.ServiceManager;
import me.timur.servicesearchtelegrambot.service.ProviderManager;
import me.timur.servicesearchtelegrambot.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

/**
 * Created by Temurbek Ismoilov on 16/05/22.
 */

@Service
@RequiredArgsConstructor
public class ProviderManagerImpl implements ProviderManager {

    private final ProviderRepository providerRepository;
    private final ServiceManager serviceManager;
    private final UserService userService;

    @Override
    public Provider getById(Long id) {
        return providerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(format("Could not find service with id %s", id)));
    }

    @Override
    public Provider getActiveById(Long id) {
        return providerRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException(format("Could not find service with id %s", id)));
    }

    @Override
    public Long save(ServiceProviderDTO dto) {
        final Provider provider = providerRepository.save(new Provider(dto));
        return provider.getId();
    }

    @Override
    public List<Provider> getAll() {
        return providerRepository.findAll();
    }

    @Override
    public void update(Long providerId, ServiceProviderDTO dto) {
        Provider provider = getById(providerId);

        final me.timur.servicesearchtelegrambot.enitity.Service service = serviceManager.getService(dto.getService().getId());
        provider.setService(service);

        providerRepository.save(provider);
    }

    @Override
    public void delete(Long providerId) {
        Provider provider = getById(providerId);
        provider.setIsActive(false);
        providerRepository.save(provider);
    }

    @Override
    public List<Provider> findAllByService(me.timur.servicesearchtelegrambot.enitity.Service service) {
        return providerRepository.findAllByServiceAndIsActiveTrue(service);
    }

    @Override
    public Provider getOrSave(UserDTO userDTO) {
        User user = userService.getOrSave(userDTO);
        Provider provider = providerRepository.findByUserTelegramId(user.getTelegramId())
                .orElse(providerRepository.save(new Provider()));
        return null;
    }
}
