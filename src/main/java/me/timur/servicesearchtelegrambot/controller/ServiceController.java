package me.timur.servicesearchtelegrambot.controller;

import lombok.AllArgsConstructor;
import me.timur.servicesearchtelegrambot.model.BaseResponse;
import me.timur.servicesearchtelegrambot.model.dto.ServiceCategoryDto;
import me.timur.servicesearchtelegrambot.service.ServiceManager;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Temurbek Ismoilov on 09/05/22.
 */

@RestController
@RequestMapping("/service")
@AllArgsConstructor
public class ServiceController {
    private final ServiceManager serviceManager;

    @PostMapping("/category")
    public BaseResponse saveServiceCategory(@RequestBody ServiceCategoryDto serviceCategoryDto){
        serviceManager.saveCategory(serviceCategoryDto);
        return BaseResponse.payload(null);
    }

    @PutMapping("/category/{category_id}")
    public BaseResponse updateServiceCategory(
            @RequestBody ServiceCategoryDto serviceCategoryDto,
            @PathVariable("category_id") Long categoryId){
        serviceManager.updateCategory(categoryId, serviceCategoryDto);
        return BaseResponse.payload(null);
    }
}
