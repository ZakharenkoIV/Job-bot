package ru.example.jobbot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.jobbot.entity.User;
import ru.example.jobbot.repository.UserRepository;
import ru.example.jobbot.service.cache.CacheService;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final CacheService cacheService;

    @Autowired
    public UserService(UserRepository userRepository, @Qualifier("hashSetCacheService") CacheService cacheService) {
        this.userRepository = userRepository;
        this.cacheService = cacheService;
    }

    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
        cacheService.addTelegramId(user.getTelegramId());
    }

    public User getUser(Long telegramId) {
        return userRepository.getUserByTelegramId(telegramId);
    }

    public boolean isUserExist(Long telegramId) {
        return cacheService.isTelegramIdExist(telegramId);
    }
}