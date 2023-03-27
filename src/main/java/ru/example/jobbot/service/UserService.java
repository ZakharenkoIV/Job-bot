package ru.example.jobbot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.jobbot.entity.TelegramUser;
import ru.example.jobbot.repository.UserRepository;

import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void saveUser(TelegramUser telegramUser) {
        userRepository.save(telegramUser);
    }

    public Optional<TelegramUser> getUser(Long telegramId) {
        return userRepository.getUserByTelegramId(telegramId);
    }
}