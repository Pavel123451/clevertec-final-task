package ru.clevertec.core.cache.impl;

import ru.clevertec.core.cache.Cache;

import java.util.LinkedHashMap;
import java.util.Map;


public class LRUCache<K, V> implements Cache<K, V> {
    private final int capacity;
    private final Map<K, V> cache;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new LinkedHashMap<>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > LRUCache.this.capacity;
            }
        };
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public void delete(K key) {
        cache.remove(key);
    }
}


