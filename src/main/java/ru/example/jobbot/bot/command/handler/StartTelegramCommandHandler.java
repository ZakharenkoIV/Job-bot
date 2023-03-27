package ru.example.jobbot.bot.command.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.example.jobbot.bot.AccessLevel;
import ru.example.jobbot.bot.keyboard.RegInlineKeyboardMarkup;
import ru.example.jobbot.service.LocalizationService;
import ru.example.jobbot.service.TelegramMessageService;
import ru.example.jobbot.service.cache.CacheService;

@Component
public class StartTelegramCommandHandler extends AbstractTelegramCommandHandler {

    private final RegInlineKeyboardMarkup keyboard;
    private final LocalizationService l10nService;

    public StartTelegramCommandHandler(RegInlineKeyboardMarkup keyboard,
                                       CacheService cacheService,
                                       TelegramMessageService messageService,
                                       LocalizationService l10nService) {
        super(
                "/start",
                AccessLevel.REG,
                cacheService,
                messageService
        );
        this.keyboard = keyboard;
        this.l10nService = l10nService;
    }

    @Override
    SendMessage createSendMessage(Update update) {
        String languageCode = update.getMessage().getFrom().getLanguageCode();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        sendMessage.setText(l10nService.getLocalizedMessage("start_command_hello_text", languageCode));
        sendMessage.setReplyMarkup(keyboard.getInlineKeyboardMarkup(languageCode));
        return sendMessage;
    }

    @Override
    public String getCommandName() {
        return super.getCommandName();
    }

    @Override
    public String getDescription(String languageCode) {
        return l10nService.getLocalizedMessage("start_command", languageCode);
    }

    @Override
    public String getAccessLevel() {
        return super.getAccessLevel();
    }
}