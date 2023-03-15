package ru.example.jobbot.bot;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class TelegramBotInitializer {

    private final TelegramBot telegramBot;
    private final TelegramBotsApi telegramBotsApi;

    @Autowired
    public TelegramBotInitializer(TelegramBot telegramBot, TelegramBotsApi telegramBotsApi) {
        this.telegramBot = telegramBot;
        this.telegramBotsApi = telegramBotsApi;
    }

    /**
     * Регистрирует бота в TelegramBotsApi и начинает получение сообщений.
     */
    @PostConstruct
    public void botConnect() {
        try {
            telegramBotsApi.registerBot(telegramBot);
            log.info("Бот зарегистрирован и готов к получению сообщений.");
        } catch (TelegramApiException e) {
            log.error("Ошибка при регистрации бота: {}", e.getMessage());
        }
    }
}