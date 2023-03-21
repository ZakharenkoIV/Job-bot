package ru.example.jobbot.bot.command.handler;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.example.jobbot.bot.TelegramBot;

public interface TelegramCommandHandler {
    String PUBLIC_SCOPE = "public";
    String PRIVATE_SCOPE = "private";

    String getCommandName();

    void handleCommand(TelegramBot bot, Update update);

    String getDescription();

    String getScope();
}