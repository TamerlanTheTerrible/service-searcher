package me.timur.servicesearchtelegrambot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidInputException extends BaseException {

    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(Throwable throwable) {super(throwable);}

    public InvalidInputException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
