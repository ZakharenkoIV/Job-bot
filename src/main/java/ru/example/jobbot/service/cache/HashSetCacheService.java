package ru.example.jobbot.service.cache;

import org.springframework.stereotype.Service;
import ru.example.jobbot.repository.UserRepository;

import java.util.HashSet;

@Service
public class HashSetCacheService implements CacheService {

    private final HashSet<Long> cache;

    public HashSetCacheService(UserRepository users) {
        cache = new HashSet<>(users.getAllTelegramIds());
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
