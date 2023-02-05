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

    List<Service> getActiveServices();

    List<String> getActiveServiceNames();

    List<String> getActiveCategoryNames();

    Service getServiceByName(String name);

    Long saveService(ServiceDTO dto);

    void updateService(Long serviceId, ServiceDTO dto);

    List<String> getServicesNamesByCategoryName(String categoryName);

    List<Service> getAllServicesByActiveTrueAndNameLike(String name);

    void deactivateService(Long serviceId);

    List<ServiceCategory> getAllCategories();

    Long saveCategory(ServiceCategoryDTO serviceCategoryDto);

    void updateCategory(Long categoryId, ServiceCategoryDTO serviceCategoryDto);

    ServiceCategory getServiceCategory(Long id);

    ServiceCategory getServiceCategory(String name);

    void deactivateServiceCategory(Long categoryId);

}
