package ru.example.jobbot.bot.keyboard.button.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.example.jobbot.bot.AccessLevel;
import ru.example.jobbot.bot.TelegramBot;
import ru.example.jobbot.entity.TelegramUser;

@Component
public class AdminInlineButtonHandler implements ButtonHandler {

    private final String accessLevel;
    private final String buttonHandlerName;

    public AdminInlineButtonHandler() {
        this.accessLevel = AccessLevel.PUBLIC;
        this.buttonHandlerName = "button_contact_with_admin";
    }

    @Override
    public String getButtonHandlerName() {
        return buttonHandlerName;
    }

    @Override
    public String getAccessLevel() {
        return accessLevel;
    }

    @Override
    public void handleButtonPress(TelegramBot bot, Update update, TelegramUser clickedUser) {
    }
}
