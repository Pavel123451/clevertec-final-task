package ru.clevertec.exceptionhandlestarter.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import ru.clevertec.exceptionhandlestarter.advice.ExceptionHandlerAdvice;

@AutoConfiguration
public class ExceptionHandlerAutoConfiguration {

    @Bean
    public ExceptionHandlerAdvice exceptionHandlerAdvice() {
        return new ExceptionHandlerAdvice();
    }
}
