package ru.example.jobbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.example.jobbot.entity.TelegramUser;

import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<TelegramUser, Long> {

    TelegramUser getUserByTelegramId(Long telegramId);

    @Query("SELECT u.telegramId FROM TelegramUser u")
    Set<Long> getAllTelegramIds();
}