package ru.example.jobbot.service.cache;

import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class HashSetCacheService implements CacheService {

    private final HashSet<Long> cache;

    public HashSetCacheService() {
        this.cache = new HashSet<>();
    }

    @Override
    public void addTelegramId(Long telegramId) {
        cache.add(telegramId);
    }

    @Override
    public boolean isTelegramIdExist(Long telegramId) {
        return cache.contains(telegramId);
    }
}
