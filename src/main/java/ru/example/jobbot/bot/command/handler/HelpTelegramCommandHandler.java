package ru.example.jobbot.bot.command.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.example.jobbot.bot.AccessLevel;
import ru.example.jobbot.service.TelegramMessageService;
import ru.example.jobbot.service.cache.CacheService;

@Component
public class HelpTelegramCommandHandler extends AbstractTelegramCommandHandler {

    public HelpTelegramCommandHandler(CacheService cacheService, TelegramMessageService messageService) {
        super("/help", "Помощь", AccessLevel.PUBLIC, cacheService, messageService);
    }

    @Override
    SendMessage createSendMessage(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        sendMessage.setText("Вызвана команда /help");
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