package me.timur.servicesearchtelegrambot.controller;

import lombok.AllArgsConstructor;
import me.timur.servicesearchtelegrambot.enitity.Service;
import me.timur.servicesearchtelegrambot.enitity.ServiceCategory;
import me.timur.servicesearchtelegrambot.model.BaseResponse;
import me.timur.servicesearchtelegrambot.model.dto.ServiceCategoryDto;
import me.timur.servicesearchtelegrambot.model.dto.ServiceDto;
import me.timur.servicesearchtelegrambot.service.ServiceManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Temurbek Ismoilov on 09/05/22.
 */

@RestController
@RequestMapping("/service")
@AllArgsConstructor
public class ServiceController {
    private final ServiceManager serviceManager;

    @GetMapping("/{serviceId}")
    public BaseResponse getService(@PathVariable Long serviceId) {
        Service service = serviceManager.getService(serviceId);
        return BaseResponse.payload(new ServiceDto(service));
    }

    @GetMapping("/category/{categoryId}")
    public BaseResponse getServicesByCategory(@PathVariable Long categoryId) {
        List<Service> services = serviceManager.getAllServicesByCategory(categoryId);
        List<ServiceDto> serviceDtos = services.stream().map(ServiceDto::new).toList();
        return BaseResponse.payload(serviceDtos);
    }

    @PostMapping("")
    public BaseResponse saveService(@RequestBody ServiceDto dto) {
        serviceManager.saveService(dto);
        return BaseResponse.payload(null);
    }

    @PutMapping("/{serviceId}")
    public BaseResponse updateService(
            @RequestBody ServiceDto dto,
            @PathVariable Long serviceId) {
        serviceManager.updateService(serviceId, dto);
        return BaseResponse.payload(null);
    }


    @DeleteMapping("/{serviceId}")
    public BaseResponse deactivateServic(@PathVariable Long serviceId){
        serviceManager.deactivateService(serviceId);
        return BaseResponse.payload(null);
    }


    @GetMapping("/category")
    public BaseResponse getAllCategories() {
        List<ServiceCategory> categories = serviceManager.getAllCategories();
        List<ServiceCategoryDto> categoryDtoList = categories.stream().map(ServiceCategoryDto::new).toList();
        return BaseResponse.payload(categoryDtoList);
    }

    @PostMapping("/category")
    public BaseResponse saveServiceCategory(@RequestBody ServiceCategoryDto serviceCategoryDto){
        serviceManager.saveCategory(serviceCategoryDto);
        return BaseResponse.payload(null);
    }

    @PutMapping("/category/{categoryId}")
    public BaseResponse updateServiceCategory(
            @RequestBody ServiceCategoryDto serviceCategoryDto,
            @PathVariable Long categoryId){
        serviceManager.updateCategory(categoryId, serviceCategoryDto);
        return BaseResponse.payload(null);
    }

    @DeleteMapping("/category/{categoryId}")
    public BaseResponse deactivateServiceCategory(@PathVariable Long categoryId){
        serviceManager.deactivateServiceCategory(categoryId);
        return BaseResponse.payload(null);
    }

}
