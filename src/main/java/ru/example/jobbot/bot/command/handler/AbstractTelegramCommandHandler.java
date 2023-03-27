package ru.example.jobbot.bot.command.handler;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.example.jobbot.bot.TelegramBot;
import ru.example.jobbot.entity.TelegramUser;
import ru.example.jobbot.service.TelegramMessageService;
import ru.example.jobbot.service.cache.CacheService;

import java.util.Optional;

@Slf4j
public abstract class AbstractTelegramCommandHandler implements TelegramCommandHandler {

    private final String commandName;
    private final String accessLevel;
    private final CacheService cacheService;
    private final TelegramMessageService messageService;


    protected AbstractTelegramCommandHandler(String commandName, String accessLevel, CacheService cacheService, TelegramMessageService messageService) {
        this.commandName = commandName;
        this.accessLevel = accessLevel;
        this.cacheService = cacheService;
        this.messageService = messageService;
    }

    @Override
    public void handleCommand(TelegramBot bot, Update update, TelegramUser clickedUser) {
        deletePreviousSameCommandMessage(bot, clickedUser);
        sendCommandMessage(bot, update, clickedUser);
    }

    private void sendCommandMessage(TelegramBot bot, Update update, TelegramUser clickedUser) {
        SendMessage sendMessage = createSendMessage(update);
        messageService.sendMessage(bot, clickedUser.getTelegramId(), clickedUser.getAccessLevel(), commandName, sendMessage);
    }

    private void deletePreviousSameCommandMessage(TelegramBot bot, TelegramUser clickedUser) {
        long chatId = clickedUser.getTelegramChatId();
        Optional<Integer> messageId = cacheService.getSentMessageId(chatId, clickedUser.getAccessLevel(), commandName);
        messageService.deleteMessage(bot, chatId, messageId);
    }

    @Override
    public String getCommandName() {
        return commandName;
    }

    @Override
    public String getAccessLevel() {
        return accessLevel;
    }

    abstract SendMessage createSendMessage(Update update);
}
