package me.timur.servicesearchtelegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import me.timur.servicesearchtelegrambot.enitity.Service;
import me.timur.servicesearchtelegrambot.enitity.ServiceCategory;
import me.timur.servicesearchtelegrambot.exception.ResourceNotFoundException;
import me.timur.servicesearchtelegrambot.model.dto.ServiceCategoryDTO;
import me.timur.servicesearchtelegrambot.model.dto.ServiceDTO;
import me.timur.servicesearchtelegrambot.repository.ServiceCategoryRepository;
import me.timur.servicesearchtelegrambot.repository.ServiceRepository;
import me.timur.servicesearchtelegrambot.service.ServiceManager;
import me.timur.servicesearchtelegrambot.util.StringUtil;
import org.springframework.cache.annotation.Cacheable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * Created by Temurbek Ismoilov on 09/05/22.
 */

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceManagerImpl implements ServiceManager {
    private final ServiceRepository serviceRepository;
    private final ServiceCategoryRepository serviceCategoryRepository;

    private final static double MINIMUM_SIMILARITY_COEFFICIENT = 0.5;

    @Override
    public Service getService(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new  ResourceNotFoundException(format("Could not find service with id %s", id)));
    }

    @Override
    public Service getActiveServiceById(Long id) {
        return serviceRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new  ResourceNotFoundException(format("Could not find service with id %s", id)));
    }

    @Override
    public List<Service> getActiveServices() {
        return serviceRepository.findAllByActiveTrue();
    }

    @Override
    @Cacheable("serviceNames")
    public List<String> getActiveServiceNames() {
        return getActiveServices().stream().map(Service::getName).collect(Collectors.toList());
    }

    @Override
    public List<String> getActiveCategoryNames() {
        return serviceCategoryRepository.findAllByActiveTrue()
                .stream().map(ServiceCategory::getName)
                .collect(Collectors.toList());
    }

    @Override
    public Service getServiceByName(String name) {
        return serviceRepository
                .findByName(name)
                .orElse(null);
    }

    @Override
    public Long saveService(ServiceDTO serviceDto) {
        ServiceCategory category = getServiceCategory(serviceDto.getCategory().getId());
        Service service = serviceRepository.save(new Service(serviceDto, category));
        return service.getId();
    }

    @Override
    public void updateService(Long serviceId, ServiceDTO dto) {
        Service service = getService(serviceId);
        service.setName(dto.getName().trim().toUpperCase());
        service.getCategory().setId(dto.getCategory().getId());
        service.setNameUz(dto.getNameUz());
        service.setNameRu(dto.getNameRu());
        serviceRepository.save(service);
    }

    @Override
    public List<String> getServicesNamesByCategoryName(String categoryName) {
        return serviceRepository.findAllByCategoryName(categoryName).stream().map(Service::getName).collect(Collectors.toList());
    }

    @Override
    public List<Service> getAllServicesByActiveTrueAndNameLike(String name) {
        List<Service> allServices = getActiveServices();
        List<Service> similarServices = new ArrayList<>();
        for (Service service: allServices) {
            double similarity = StringUtil.findSimilarities(service.getNameUz(), name);
            if (similarity > MINIMUM_SIMILARITY_COEFFICIENT){
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
    public Long saveCategory(ServiceCategoryDTO serviceCategoryDto) {
        ServiceCategory category = serviceCategoryRepository.save(new ServiceCategory(serviceCategoryDto));
        return category.getId();
    }

    @Override
    public void updateCategory(Long categoryId, ServiceCategoryDTO serviceCategoryDto) {
        ServiceCategory category = getServiceCategory(categoryId);
        category.setName(serviceCategoryDto.getName().trim().toUpperCase());
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
