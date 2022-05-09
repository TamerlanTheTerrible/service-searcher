package me.timur.servicesearchtelegrambot.exception;

/**
 * Created by Temurbek Ismoilov on 07/02/22.
 */

public class BaseException extends RuntimeException{

    public BaseException(String message) {
        super(message);
    }

    public BaseException(Throwable throwable) {
        super(throwable);
    }

    public BaseException(String message, Throwable throwable) {
        super(String.format("%s %n %s", throwable.getMessage(), message), throwable);
    }
}

