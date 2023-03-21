package ru.example.jobbot.bot.update;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeChat;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.example.jobbot.bot.TelegramBot;
import ru.example.jobbot.bot.command.TelegramCommandFilter;
import ru.example.jobbot.bot.command.handler.TelegramCommandHandler;
import ru.example.jobbot.bot.controller.AccessController;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class TelegramUpdateHandler {

    private final AccessController accessController;
    private final TelegramCommandFilter commandFilter;

    public void handleUpdate(TelegramBot bot, Update update) {
        if (hasMessageFromUser(update)) {
            long telegramUserId = update.getMessage().getFrom().getId();
            long chatId = update.getMessage().getChatId();
            List<TelegramCommandHandler> allowedCommands = getAccessibleCommandHandlers(bot, telegramUserId, chatId);
            String commandName = extractText(update);
            executeCommand(bot, commandName, allowedCommands, update);
        }
    }

    private boolean hasMessageFromUser(Update update) {
        return update.hasMessage() && update.getMessage().getFrom() != null;
    }

    private List<TelegramCommandHandler> getAccessibleCommandHandlers(TelegramBot bot, long telegramUserId, long chatId) {
        if (accessController.checkAccess(telegramUserId)) {
            return setAllowedCommands(bot, chatId, TelegramCommandHandler.PUBLIC_SCOPE, TelegramCommandHandler.PRIVATE_SCOPE);
        } else {
            return setAllowedCommands(bot, chatId, TelegramCommandHandler.PUBLIC_SCOPE);
        }
    }

    private List<TelegramCommandHandler> setAllowedCommands(TelegramBot bot, Long chatId, String... scope) {
        sendBotCommands(bot, chatId, scope);
        return getAllowedCommands(scope);
    }

    private void sendBotCommands(TelegramBot bot, Long chatId, String... scopes) {
        if (!accessController.checkRegisterChatId(chatId)) {
            List<BotCommand> botCommands = Arrays.stream(scopes)
                    .flatMap(scope -> commandFilter.getBotCommands(scope).stream())
                    .collect(Collectors.toList());
            setBotCommandsForPrivateChat(bot, chatId, botCommands);
            accessController.addChatId(chatId);
        }
    }

    private List<TelegramCommandHandler> getAllowedCommands(String... scopes) {
        return Arrays.stream(scopes)
                .flatMap(scope -> commandFilter.getCommands(scope).stream())
                .collect(Collectors.toList());
    }

    private void setBotCommandsForPrivateChat(TelegramBot bot, Long chatId, List<BotCommand> commands) {
        SetMyCommands setMyCommands = SetMyCommands.builder()
                .commands(commands)
                .scope(new BotCommandScopeChat(String.valueOf(chatId)))
                .languageCode("ru")
                .build();
        try {
            bot.execute(setMyCommands);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String extractText(Update update) {
        return Optional.ofNullable(update)
                .map(Update::getMessage)
                .filter(Message::hasText)
                .map(Message::getText)
                .orElse(null);
    }

    private void executeCommand(TelegramBot bot, String commandName, List<TelegramCommandHandler> allCommands, Update update) {
        allCommands.stream()
                .filter(command -> command.getCommandName().equals(commandName))
                .findFirst()
                .ifPresent(command -> command.handleCommand(bot, update));
    }
}