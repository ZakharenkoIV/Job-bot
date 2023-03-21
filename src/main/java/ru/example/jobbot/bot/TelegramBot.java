package ru.example.jobbot.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import ru.example.jobbot.bot.update.TelegramUpdateHandler;
import ru.example.jobbot.config.TelegramBotProperties;

@Slf4j
@Component
public class TelegramBot extends DefaultAbsSender implements LongPollingBot {

    private final TelegramBotProperties properties;
    private final TelegramUpdateHandler updateHandler;

    @Autowired
    public TelegramBot(TelegramBotProperties properties, TelegramUpdateHandler updateHandler) {
        super(new DefaultBotOptions(), properties.getToken());
        this.properties = properties;
        this.updateHandler = updateHandler;
    }

    @Override
    public String getBotUsername() {
        return properties.getUsername();
    }

    @Override
    public void onUpdateReceived(Update update) {
        updateHandler.handleUpdate(this, update);
    }

    @Override
    public void clearWebhook() {
    }

}