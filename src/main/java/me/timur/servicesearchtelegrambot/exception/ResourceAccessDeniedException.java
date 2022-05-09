package me.timur.servicesearchtelegrambot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Temurbek Ismoilov on 25/02/22.
 */

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ResourceAccessDeniedException extends BaseException {
    public ResourceAccessDeniedException(String message) {
        super(message);
    }

    public ResourceAccessDeniedException(Throwable throwable) {
        super(throwable);
    }

    public ResourceAccessDeniedException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
