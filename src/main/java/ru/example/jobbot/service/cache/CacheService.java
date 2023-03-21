package ru.example.jobbot.service.cache;

public interface CacheService {
    void addTelegramId(Long telegramId);

    boolean isTelegramIdExist(Long telegramId);

    boolean isChatIdExist(Long chatId);

    void addChatId(Long chatId);
}
