package ru.example.jobbot.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import ru.example.jobbot.bot.command.TelegramCommandHandler;
import ru.example.jobbot.bot.controller.AccessController;
import ru.example.jobbot.config.TelegramBotProperties;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TelegramBot extends DefaultAbsSender implements LongPollingBot {

    private final TelegramBotProperties properties;
    private final Map<String, TelegramCommandHandler> commandHandlers;
    private final AccessController accessController;

    @Autowired
    public TelegramBot(TelegramBotProperties properties, List<TelegramCommandHandler> handlers, AccessController accessController) {
        super(new DefaultBotOptions(), properties.getToken());
        this.properties = properties;
        this.commandHandlers = handlers.stream()
                .collect(Collectors.toMap(TelegramCommandHandler::getCommandName, Function.identity()));
        this.accessController = accessController;
    }

    @Override
    public String getBotUsername() {
        return properties.getUsername();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (accessController.checkAccess(update.getMessage().getFrom().getId())) {
            Optional.ofNullable(extractText(update))
                    .map(commandHandlers::get)
                    .ifPresent(handler -> handler.handleCommand(this, update));
        }
    }

    @Override
    public void clearWebhook() {
    }

    private String extractText(Update update) {
        return Optional.ofNullable(update)
                .filter(Update::hasMessage)
                .map(Update::getMessage)
                .filter(Message::hasText)
                .map(Message::getText)
                .orElse(null);
    }
}