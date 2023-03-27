package ru.example.jobbot.bot;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeChat;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.example.jobbot.bot.command.handler.TelegramCommandHandler;
import ru.example.jobbot.bot.keyboard.button.handler.ButtonHandler;
import ru.example.jobbot.entity.TelegramUser;
import ru.example.jobbot.service.cache.CacheService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TelegramCommandFilter {

    private final Map<String, ButtonHandler> telegramButtonHandlers;
    private final Set<TelegramCommandHandler> telegramCommands;
    private final CacheService cacheService;

    public TelegramCommandFilter(@Qualifier("buttonHandlersMap") Map<String, ButtonHandler> buttonHandlers, Set<TelegramCommandHandler> telegramCommands, CacheService cacheService) {
        this.telegramButtonHandlers = buttonHandlers;
        this.telegramCommands = telegramCommands;
        this.cacheService = cacheService;
    }

    public void handleCommandEvent(TelegramBot bot, Update update, TelegramUser clickedUser) {
        String commandName = getCommandName(update);
        executeCommand(bot, update, commandName, clickedUser);
        sendMenuCommand(bot, update, clickedUser);
    }

    private void sendMenuCommand(TelegramBot bot, Update update, TelegramUser clickedUser) {
        String languageCode = getLanguageCode(update);
        if (update.hasMessage()) {
            if (!cacheService.isChatIdExist(clickedUser.getTelegramId())) {
                List<BotCommand> allowedBotCommand = telegramCommands.stream()
                        .filter(command -> clickedUser.getAccessLevel().contains(command.getAccessLevel()))
                        .map(command -> new BotCommand(command.getCommandName(), command.getDescription(languageCode)))
                        .collect(Collectors.toList());
                sendBotCommandsForPrivateChat(bot, clickedUser.getTelegramId(), allowedBotCommand, languageCode);
                cacheService.addChatId(clickedUser.getTelegramId());
            }
        }
    }

    private String getLanguageCode(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getFrom().getLanguageCode();
        }
        return "default";
    }

    private void sendBotCommandsForPrivateChat(TelegramBot bot, Long chatId, List<BotCommand> commands, String languageCode) {
        SetMyCommands setMyCommands = SetMyCommands.builder()
                .commands(commands)
                .scope(new BotCommandScopeChat(String.valueOf(chatId)))
                .languageCode(languageCode)
                .build();
        try {
            bot.execute(setMyCommands);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void executeCommand(TelegramBot bot, Update update, String commandName, TelegramUser clickedUser) {
        if (telegramButtonHandlers.containsKey(commandName)
                && clickedUser.getAccessLevel().contains(telegramButtonHandlers.get(commandName).getAccessLevel())) {
            telegramButtonHandlers.get(commandName).handleButtonPress(bot, update, clickedUser);
        } else {
            telegramCommands.stream()
                    .filter(command -> command.getCommandName().equals(commandName)
                            && clickedUser.getAccessLevel().contains(command.getAccessLevel()))
                    .findAny()
                    .ifPresent(command -> command.handleCommand(bot, update, clickedUser));
        }
    }

    private String getCommandName(Update update) {
        String commandName = "";
        if (update.hasCallbackQuery()) {
            commandName = update.getCallbackQuery().getData();
            try {
                JSONObject jsonObject = new JSONObject(commandName);
                if (jsonObject.has("command_name")) {
                    commandName = jsonObject.get("command_name").toString();
                }
            } catch (JSONException ignored) {
            }
        } else {
            if (update.hasMessage() && update.getMessage().hasText()) {
                commandName = update.getMessage().getText();
            }
        }
        return commandName;
    }
}
