package me.timur.servicesearchtelegrambot.controller;

import lombok.extern.slf4j.Slf4j;
import me.timur.servicesearchtelegrambot.exception.ResourceNotFoundException;
import me.timur.servicesearchtelegrambot.model.BaseResponse;
import me.timur.servicesearchtelegrambot.model.dto.ErrorDTO;
import me.timur.servicesearchtelegrambot.model.dto.NoopDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created by Temurbek Ismoilov on 07/02/22.
 */

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public BaseResponse<ErrorDTO> handleResourceNotFoundException(ResourceNotFoundException e){
        log.error(e.getMessage());
        return BaseResponse.error(e);
    }
    
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public BaseResponse<ErrorDTO> handleUnexpectedException(Exception e){
        log.error("Unexpected exception: " + e.getMessage(), e);
        return BaseResponse.error(e);
    }
}



