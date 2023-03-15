package ru.example.jobbot.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Component
public class TelegramBotInitializer {

    private final TelegramBot telegramBot;

    @Autowired
    public TelegramBotInitializer(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
        botConnect();
    }

    public void botConnect() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(telegramBot);
            log.info("Бот зарегистрирован и готов к получению сообщений.");
        } catch (TelegramApiException e) {
            log.error("Ошибка при регистрации бота: {}", e.getMessage());
        }
    }
}