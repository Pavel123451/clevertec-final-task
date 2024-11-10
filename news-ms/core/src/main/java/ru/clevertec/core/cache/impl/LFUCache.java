package ru.clevertec.core.cache.impl;

import ru.clevertec.core.cache.Cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class LFUCache<K, V> implements Cache<K, V> {
    private final int maxSize;
    private final Map<K, V> cache = new HashMap<>();
    private final Map<K, Integer> frequencies = new HashMap<>();

    public LFUCache(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public V get(K key) {
        if (!cache.containsKey(key)) {
            return null;
        }
        frequencies.put(key, frequencies.get(key) + 1);
        return cache.get(key);
    }

    @Override
    public void put(K key, V value) {
        if (cache.size() >= maxSize) {
            K leastFrequentKey = Collections
                    .min(frequencies.entrySet(), Map.Entry.comparingByValue()).getKey();
            cache.remove(leastFrequentKey);
            frequencies.remove(leastFrequentKey);
        }
        cache.put(key, value);
        frequencies.put(key, 1);
    }

    @Override
    public void delete(K key) {
        cache.remove(key);
        frequencies.remove(key);
    }
}
