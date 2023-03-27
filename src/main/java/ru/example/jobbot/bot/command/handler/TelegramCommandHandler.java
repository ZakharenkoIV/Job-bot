package ru.example.jobbot.bot.command.handler;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.example.jobbot.bot.TelegramBot;
import ru.example.jobbot.entity.TelegramUser;

public interface TelegramCommandHandler {

    String getCommandName();

    void handleCommand(TelegramBot bot, Update update, TelegramUser user);

    String getDescription();

    String getAccessLevel();
}