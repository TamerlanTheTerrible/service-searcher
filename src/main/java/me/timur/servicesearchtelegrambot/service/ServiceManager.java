package me.timur.servicesearchtelegrambot.service;

import me.timur.servicesearchtelegrambot.enitity.Service;
import me.timur.servicesearchtelegrambot.enitity.ServiceCategory;
import me.timur.servicesearchtelegrambot.model.dto.ServiceCategoryDTO;
import me.timur.servicesearchtelegrambot.model.dto.ServiceDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Temurbek Ismoilov on 09/05/22.
 */

@Transactional
public interface ServiceManager {

    Service getService(Long serviceId);

    Service getActiveServiceById(Long id);

    List<Service> getAllActiveServices();

    Long saveService(ServiceDTO dto);

    void updateService(Long serviceId, ServiceDTO dto);

    List<Service> getAllServicesByCategory(Long serviceCategoryId);

    List<Service> getAllServicesByNameLike(String name);

    void deactivateService(Long serviceId);

    List<ServiceCategory> getAllCategories();

    Long saveCategory(ServiceCategoryDTO serviceCategoryDto);

    void updateCategory(Long categoryId, ServiceCategoryDTO serviceCategoryDto);

    ServiceCategory getServiceCategory(Long id);

    void deactivateServiceCategory(Long categoryId);

}
