package ru.clevertec.core.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import ru.clevertec.core.cache.Cache;
import ru.clevertec.core.cache.impl.LFUCache;
import ru.clevertec.core.cache.impl.LRUCache;
import ru.clevertec.core.cache.impl.RedisCache;

@Configuration
@EnableConfigurationProperties(CacheProperties.class)
@RequiredArgsConstructor
public class CacheConfig {

    private final CacheProperties cacheProperties;


    @Bean
    @Profile("!redis")
    public Cache<Long, ?> cache() {
        Cache<Long, Object> cache;
        if (cacheProperties.getAlgorithm().equalsIgnoreCase("LFU")) {
            cache = new LFUCache<>(cacheProperties.getSize());
        } else {
            cache = new LRUCache<>(cacheProperties.getSize());
        }
        return cache;
    }

    @Bean
    @Profile("redis")
    public Cache<Long, ?> redisCache(RedisTemplate<Long, Object> redisTemplate) {
        return new RedisCache<>(redisTemplate);
    }

    @Bean
    public RedisTemplate<Long, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Long, Object> template = new RedisTemplate<>();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        objectMapper.activateDefaultTyping(
                BasicPolymorphicTypeValidator.builder()
                        .allowIfSubType(Object.class)
                        .build(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );

        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(RedisSerializer.java());
        template.setValueSerializer(serializer);

        return template;
    }




}
