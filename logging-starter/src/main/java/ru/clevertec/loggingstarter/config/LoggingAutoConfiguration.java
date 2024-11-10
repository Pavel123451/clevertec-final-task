package ru.clevertec.loggingstarter.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import ru.clevertec.loggingstarter.aspect.LoggingAspect;

@AutoConfiguration
public class LoggingAutoConfiguration {

    @Bean
    LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}
