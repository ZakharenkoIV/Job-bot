package ru.example.jobbot.bot.keyboard.button.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.example.jobbot.bot.TelegramBot;
import ru.example.jobbot.entity.TelegramUser;

@Component
public interface ButtonHandler {

    String getButtonHandlerName();

    void handleButtonPress(TelegramBot bot, Update update, TelegramUser clickedUser);

    String getAccessLevel();
}