package ru.clevertec.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cache")
@Getter
@Setter
public class CacheProperties {
    private String algorithm = "LRU";
    private int size = 100;
}

