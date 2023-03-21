package ru.example.jobbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.example.jobbot.entity.User;

import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User getUserByTelegramId(Long telegramId);

    @Query("SELECT u.telegramId FROM User u")
    Set<Long> getAllTelegramIds();
}