package ru.example.jobbot.component;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.example.jobbot.config.TelegramBotProperties;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final String botUsername;

    public TelegramBot(TelegramBotProperties properties) {
        super(properties.getToken());
        this.botUsername = properties.getUsername();
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {

    }
}