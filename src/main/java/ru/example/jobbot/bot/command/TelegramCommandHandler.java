package ru.example.jobbot.bot.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.example.jobbot.bot.TelegramBot;

public interface TelegramCommandHandler {
    String getCommandName();

    void handleCommand(TelegramBot bot, Update update);

}