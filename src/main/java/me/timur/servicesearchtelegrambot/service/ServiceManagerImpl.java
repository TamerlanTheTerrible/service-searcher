package me.timur.servicesearchtelegrambot.service;

import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.enitity.ServiceCategory;
import me.timur.servicesearchtelegrambot.enitity.Service;
import me.timur.servicesearchtelegrambot.exception.ResourceNotFoundException;
import me.timur.servicesearchtelegrambot.model.dto.ServiceCategoryDTO;
import me.timur.servicesearchtelegrambot.model.dto.ServiceDTO;
import me.timur.servicesearchtelegrambot.repository.ServiceCategoryRepository;
import me.timur.servicesearchtelegrambot.repository.ServiceRepository;
import me.timur.servicesearchtelegrambot.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

/**
 * Created by Temurbek Ismoilov on 09/05/22.
 */

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceManagerImpl implements ServiceManager {
    private final ServiceRepository serviceRepository;
    private final ServiceCategoryRepository serviceCategoryRepository;


    @Override
    public Service getService(Long id) {
        return serviceRepository
                .findById(id)
                .orElseThrow(() -> new  ResourceNotFoundException(format("Could not find service with id %s", id)));
    }

    @Override
    public List<Service> getAllActiveServices() {
        return serviceRepository.findAllByActiveTrue();
    }

    @Override
    public void saveService(ServiceDTO serviceDto) {
        ServiceCategory category = getServiceCategory(serviceDto.getCategory().getId());
        serviceRepository.save(new Service(serviceDto, category));
    }

    @Override
    public void updateService(Long serviceId, ServiceDTO dto) {
        Service service = getService(serviceId);
        service.setName(dto.getName().trim().toUpperCase());
        service.getCategory().setId(dto.getCategory().getId());
        service.setLang(dto.getLang());
        serviceRepository.save(service);
    }

    @Override
    public List<Service> getAllServicesByCategory(Long serviceCategoryId) {
        return serviceRepository.findAllByCategoryId(serviceCategoryId);
    }

    @Override
    public List<Service> getAllServicesByNameLike(String name) {
        List<Service> allServices = getAllActiveServices();
        List<Service> similarServices = new ArrayList<>();
        double minimumSimilarity = 0.5;
        for (Service service: allServices) {
            double similarity = StringUtil.findSimilarities(service.getLang().getUz(), name);
            if (similarity > minimumSimilarity){
                similarServices.add(service);
            }
        }
        return similarServices;
    }

    @Override
    public void deactivateService(Long serviceId) {
        Service service = getService(serviceId);
        service.setActive(false);
        serviceRepository.save(service);
    }

    @Override
    public List<ServiceCategory> getAllCategories() {
        return serviceCategoryRepository.findAllByActiveTrue();
    }

    @Override
    public void saveCategory(ServiceCategoryDTO serviceCategoryDto) {
        serviceCategoryRepository.save(new ServiceCategory(serviceCategoryDto));
    }

    @Override
    public void updateCategory(Long categoryId, ServiceCategoryDTO serviceCategoryDto) {
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

    @Override
    public void deactivateServiceCategory(Long categoryId) {
        ServiceCategory category = getServiceCategory(categoryId);
        category.setActive(false);
        serviceCategoryRepository.save(category);
    }

}
