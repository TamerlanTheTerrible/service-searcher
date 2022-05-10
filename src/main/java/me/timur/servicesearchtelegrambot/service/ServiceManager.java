package me.timur.servicesearchtelegrambot.service;

import me.timur.servicesearchtelegrambot.enitity.Service;
import me.timur.servicesearchtelegrambot.enitity.ServiceCategory;
import me.timur.servicesearchtelegrambot.model.dto.ServiceCategoryDto;
import me.timur.servicesearchtelegrambot.model.dto.ServiceDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Temurbek Ismoilov on 09/05/22.
 */

@Transactional
public interface ServiceManager {

    Service getService(Long serviceId);

    List<Service> getAllActiveServices();

    void saveService(ServiceDto dto);

    void updateService(Long serviceId, ServiceDto dto);

    List<Service> getAllServicesByCategory(Long serviceCategoryId);

    List<Service> getAllServicesByNameLike(String name);

    void deactivateService(Long serviceId);

    List<ServiceCategory> getAllCategories();

    void saveCategory(ServiceCategoryDto serviceCategoryDto);

    void updateCategory(Long categoryId, ServiceCategoryDto serviceCategoryDto);

    ServiceCategory getServiceCategory(Long id);

    void deactivateServiceCategory(Long categoryId);

}
