package ru.clevertec.core.cache.impl;

import org.springframework.data.redis.core.RedisTemplate;
import ru.clevertec.core.cache.Cache;

public class RedisCache<K, V> implements Cache<K, V> {

    private final RedisTemplate<K, V> redisTemplate;

    public RedisCache(RedisTemplate<K, V> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public V get(K key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void put(K key, V value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void delete(K key) {
        redisTemplate.delete(key);
    }
}

