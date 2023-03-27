package ru.example.jobbot.service.cache;

import ru.example.jobbot.entity.TelegramUser;

import java.util.Map;
import java.util.Optional;

public interface CacheService {
    void addTelegramId(Long telegramId);

    boolean isTelegramIdExist(Long telegramId);

    boolean isChatIdExist(Long chatId);

    void addChatId(Long chatId);

    void addRegistrationMap(TelegramUser user);

    TelegramUser removeTelegramUserFromRegistrationMap(Long user);

    void removeRegistrationMap(Long user);

    void removeChatId(Long chatId);

    Optional<Integer> getSentMessageId(Long telegramUserId, String accessLevel, String commandName);

    void putSentMessageId(Long chatId, String accessLevel, String commandName, Integer messageId);

    boolean containsCommandMassage(Long telegramUserId, String accessLevel, String commandName);
}