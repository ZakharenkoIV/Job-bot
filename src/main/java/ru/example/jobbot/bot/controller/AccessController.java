package ru.example.jobbot.bot.controller;

import org.springframework.stereotype.Component;
import ru.example.jobbot.service.cache.CacheService;

@Component
public class AccessController {
    private final CacheService cacheService;

    public AccessController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public boolean checkAccess(long telegramId) {
        return cacheService.isTelegramIdExist(telegramId);
    }
}