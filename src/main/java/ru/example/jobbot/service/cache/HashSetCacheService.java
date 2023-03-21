package ru.example.jobbot.service.cache;

import org.springframework.stereotype.Service;
import ru.example.jobbot.entity.TelegramUser;
import ru.example.jobbot.repository.UserRepository;

import java.util.HashSet;
import java.util.List;

@Service
public class HashSetCacheService implements CacheService {

    private final HashSet<Long> telegramIds;
    private final HashSet<Long> chatIds;
    private final List<TelegramUser> users;


    public HashSetCacheService(UserRepository userRepository) {
        telegramIds = new HashSet<>(userRepository.getAllTelegramIds());
        chatIds = new HashSet<>();
        users = userRepository.findAll();
    }

    @Override
    public void addTelegramId(Long telegramId) {
        telegramIds.add(telegramId);
    }

    @Override
    public boolean isTelegramIdExist(Long telegramId) {
        return telegramIds.contains(telegramId);
    }

    public List<TelegramUser> getUsers() {
        return users;
    }

    public HashSet<Long> getChatIds() {
        return chatIds;
    }

    public boolean isChatIdExist(Long chatId) {
        return chatIds.contains(chatId);
    }

    @Override
    public void addChatId(Long chatId) {
        this.chatIds.add(chatId);
    }
}
