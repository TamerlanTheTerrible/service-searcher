package me.timur.servicesearchtelegrambot.controller;

import lombok.AllArgsConstructor;
import me.timur.servicesearchtelegrambot.enitity.ServiceProvider;
import me.timur.servicesearchtelegrambot.model.BaseResponse;
import me.timur.servicesearchtelegrambot.model.dto.NoopDTO;
import me.timur.servicesearchtelegrambot.model.dto.ServiceProviderDTO;
import me.timur.servicesearchtelegrambot.service.ServiceProviderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Temurbek Ismoilov on 16/05/22.
 */


@RestController
@RequestMapping("/provider")
@AllArgsConstructor
public class ServiceProviderController {

    private final ServiceProviderService providerService;

    @PostMapping("")
    public BaseResponse<Long> save(@RequestBody ServiceProviderDTO dto) {
        Long id = providerService.save(dto);
        return BaseResponse.payload(id);
    }

    @GetMapping("")
    public BaseResponse<List<ServiceProviderDTO>> getAll() {
        List<ServiceProvider> providers = providerService.getAll();
        List<ServiceProviderDTO> providerDTOS = providers.stream().map(ServiceProviderDTO::new).toList();
        return BaseResponse.payload(providerDTOS);
    }

    @GetMapping("/{providerId}")
    public BaseResponse<ServiceProviderDTO> getById(@PathVariable Long providerId) {
        ServiceProvider provider = providerService.getById(providerId);
        return BaseResponse.payload(new ServiceProviderDTO(provider));
    }

    @PutMapping("/{providerId}")
    public BaseResponse<NoopDTO> update(@PathVariable Long providerId, @RequestBody ServiceProviderDTO dto) {
        providerService.update(providerId, dto);
        return BaseResponse.payload();
    }

    @DeleteMapping("/{providerId}")
    public BaseResponse<NoopDTO> delete(@PathVariable Long providerId) {
        providerService.delete(providerId);
        return BaseResponse.payload();
    }
}
