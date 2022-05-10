package me.timur.servicesearchtelegrambot.service;

import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.enitity.ServiceCategory;
import me.timur.servicesearchtelegrambot.exception.ResourceNotFoundException;
import me.timur.servicesearchtelegrambot.model.dto.ServiceCategoryDto;
import me.timur.servicesearchtelegrambot.repository.ServiceCategoryRepository;
import me.timur.servicesearchtelegrambot.repository.ServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

/**
 * Created by Temurbek Ismoilov on 09/05/22.
 */

@Service
@RequiredArgsConstructor
public class ServiceManagerImpl implements ServiceManager {
    private final ServiceRepository serviceRepository;
    private final ServiceCategoryRepository serviceCategoryRepository;

    @Override
    public List<ServiceCategory> getAllCategories() {
        return serviceCategoryRepository.findAllByActiveTrue();
    }

    @Override
    public void saveCategory(ServiceCategoryDto serviceCategoryDto) {
        serviceCategoryRepository.save(new ServiceCategory(serviceCategoryDto));
    }

    @Override
    public void updateCategory(Long categoryId, ServiceCategoryDto serviceCategoryDto) {
        ServiceCategory category = getServiceCategory(categoryId);
        category.setName(serviceCategoryDto.getName().trim().toUpperCase());
        category.setLang(serviceCategoryDto.getLang());
        serviceCategoryRepository.save(category);
    }

    @Override
    public ServiceCategory getServiceCategory(Long id) {
        return serviceCategoryRepository
                .findById(id)
                .orElseThrow(() -> new  ResourceNotFoundException(format("Could not find service category with id %s", id)));
    }

}
