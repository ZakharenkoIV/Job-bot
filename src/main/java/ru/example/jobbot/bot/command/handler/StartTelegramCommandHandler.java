package ru.example.jobbot.bot.command.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.example.jobbot.bot.AccessLevel;
import ru.example.jobbot.bot.keyboard.RegInlineKeyboardMarkup;
import ru.example.jobbot.service.TelegramMessageService;
import ru.example.jobbot.service.cache.CacheService;

@Component
public class StartTelegramCommandHandler extends AbstractTelegramCommandHandler {

    private final InlineKeyboardMarkup regKeyboard;

    public StartTelegramCommandHandler(RegInlineKeyboardMarkup keyboard, CacheService cacheService, TelegramMessageService messageService) {
        super("/start", "Знакомство", AccessLevel.REG, cacheService, messageService);
        this.regKeyboard = keyboard.getInlineKeyboardMarkup();
    }

    @Override
    SendMessage createSendMessage(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        sendMessage.setText("Привет! Это начало работы с ботом.");
        sendMessage.setReplyMarkup(regKeyboard);
        return sendMessage;
    }

    @Override
    public String getCommandName() {
        return super.getCommandName();
    }

    @Override
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    public String getAccessLevel() {
        return super.getAccessLevel();
    }
}