package ru.clevertec.exceptionhandlestarter.exception;

public class NewsNotFoundException extends RuntimeException{
    public NewsNotFoundException(String message) {
        super(message);
    }
}
