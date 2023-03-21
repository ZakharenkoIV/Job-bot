package ru.example.jobbot.service.cache;

public interface CacheService {
    void addTelegramId(Long telegramId);

    boolean isTelegramIdExist(Long telegramId);
}
