package me.timur.servicesearchtelegrambot.controller;

import me.timur.servicesearchtelegrambot.model.BaseResponse;
import me.timur.servicesearchtelegrambot.model.dto.QueryDto;
import me.timur.servicesearchtelegrambot.service.QueryService;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Temurbek Ismoilov on 24/04/22.
 */

@RestController
@RequestMapping("/client")
public record ClientController(
        QueryService queryService
) {

    @PostMapping
    public BaseResponse saveQuery(@RequestBody QueryDto dto){
        queryService.save(dto);
        return BaseResponse.payload(null);
    }
}
