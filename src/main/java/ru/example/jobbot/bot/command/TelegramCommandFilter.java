package ru.example.jobbot.bot.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import ru.example.jobbot.bot.command.handler.TelegramCommandHandler;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TelegramCommandFilter {

    private final List<TelegramCommandHandler> telegramCommands;

    @Autowired
    public TelegramCommandFilter(List<TelegramCommandHandler> telegramCommands) {
        this.telegramCommands = telegramCommands;
    }

    public List<BotCommand> getBotCommands(String scope) {
        return getCommands(scope).stream()
                .map(command -> new BotCommand(command.getCommandName(), command.getDescription()))
                .collect(Collectors.toList());
    }

    public List<TelegramCommandHandler> getCommands(String scope) {
        return telegramCommands.stream()
                .filter(command -> command.getScope().equals(scope))
                .collect(Collectors.toList());
    }
}
