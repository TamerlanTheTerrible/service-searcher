package me.timur.servicesearchtelegrambot.controller;

import lombok.AllArgsConstructor;
import me.timur.servicesearchtelegrambot.enitity.Service;
import me.timur.servicesearchtelegrambot.enitity.ServiceCategory;
import me.timur.servicesearchtelegrambot.model.BaseResponse;
import me.timur.servicesearchtelegrambot.model.dto.NoopDTO;
import me.timur.servicesearchtelegrambot.model.dto.ServiceCategoryDTO;
import me.timur.servicesearchtelegrambot.model.dto.ServiceDTO;
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


    @GetMapping("/find/{searchedService}")
    public BaseResponse<List<ServiceDTO>> getServiceByNameLike(@PathVariable String searchedService) {
        List<Service> services = serviceManager.getAllServicesByNameLike(searchedService);
        List<ServiceDTO> serviceDTOS = services.stream().map(ServiceDTO::new).toList();
        return BaseResponse.payload(serviceDTOS);
    }

    @GetMapping("/{serviceId}")
    public BaseResponse<ServiceDTO> getService(@PathVariable Long serviceId) {
        Service service = serviceManager.getService(serviceId);
        return BaseResponse.payload(new ServiceDTO(service));
    }

    @GetMapping("/")
    public BaseResponse<List<ServiceDTO>> getAllServices() {
        List<Service> services = serviceManager.getAllActiveServices();
        List<ServiceDTO> serviceDTOS = services.stream().map(ServiceDTO::new).toList();
        return BaseResponse.payload(serviceDTOS);
    }

    @GetMapping("/category/{categoryId}")
    public BaseResponse<List<ServiceDTO>> getServicesByCategory(@PathVariable Long categoryId) {
        List<Service> services = serviceManager.getAllServicesByCategory(categoryId);
        List<ServiceDTO> serviceDTOS = services.stream().map(ServiceDTO::new).toList();
        return BaseResponse.payload(serviceDTOS);

    }

    @PostMapping("")
    public BaseResponse<NoopDTO> saveService(@RequestBody ServiceDTO dto) {
        serviceManager.saveService(dto);
        return BaseResponse.payload();
    }

    @PutMapping("/{serviceId}")
    public BaseResponse<NoopDTO> updateService(
            @RequestBody ServiceDTO dto,
            @PathVariable Long serviceId) {
        serviceManager.updateService(serviceId, dto);
        return BaseResponse.payload();
    }

    @DeleteMapping("/{serviceId}")
    public BaseResponse<NoopDTO> deactivateService(@PathVariable Long serviceId){
        serviceManager.deactivateService(serviceId);
        return BaseResponse.payload();
    }

    @GetMapping("/category")
    public BaseResponse<List<ServiceCategoryDTO>> getAllCategories() {
        List<ServiceCategory> categories = serviceManager.getAllCategories();
        List<ServiceCategoryDTO> categoryDtoList = categories.stream().map(ServiceCategoryDTO::new).toList();
        return BaseResponse.payload(categoryDtoList);
    }

    @PostMapping("/category")
    public BaseResponse<NoopDTO> saveServiceCategory(@RequestBody ServiceCategoryDTO serviceCategoryDto){
        serviceManager.saveCategory(serviceCategoryDto);
        return BaseResponse.payload();
    }

    @PutMapping("/category/{categoryId}")
    public BaseResponse<NoopDTO> updateServiceCategory(
            @RequestBody ServiceCategoryDTO serviceCategoryDto,
            @PathVariable Long categoryId){
        serviceManager.updateCategory(categoryId, serviceCategoryDto);
        return BaseResponse.payload();
    }

    @DeleteMapping("/category/{categoryId}")
    public BaseResponse<NoopDTO> deactivateServiceCategory(@PathVariable Long categoryId){
        serviceManager.deactivateServiceCategory(categoryId);
        return BaseResponse.payload();
    }
}
