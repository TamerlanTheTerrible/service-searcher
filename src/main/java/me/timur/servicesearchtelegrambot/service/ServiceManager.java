package me.timur.servicesearchtelegrambot.service;

import me.timur.servicesearchtelegrambot.enitity.ServiceCategory;
import me.timur.servicesearchtelegrambot.model.dto.ServiceCategoryDto;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Temurbek Ismoilov on 09/05/22.
 */

@Transactional
public interface ServiceManager {

    void saveCategory(ServiceCategoryDto serviceCategoryDto);

    void updateCategory(Long categoryId, ServiceCategoryDto serviceCategoryDto);

    ServiceCategory getServiceCategory(Long id);


}
