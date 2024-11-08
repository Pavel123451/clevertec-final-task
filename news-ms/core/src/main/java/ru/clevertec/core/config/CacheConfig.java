package ru.clevertec.core.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.core.cache.Cache;
import ru.clevertec.core.cache.impl.LFUCache;
import ru.clevertec.core.cache.impl.LRUCache;

@Configuration
@EnableConfigurationProperties(CacheProperties.class)
@RequiredArgsConstructor
public class CacheConfig {

    private final CacheProperties cacheProperties;

    @Bean
    public Cache<Long, ?> cache() {
        Cache<Long, Object> cache = new LRUCache<>(cacheProperties.getSize());
        if (cacheProperties.getAlgorithm().equalsIgnoreCase("LFU")) {
            cache = new LFUCache<>(cacheProperties.getSize());
        }
        return cache;
    }
}
