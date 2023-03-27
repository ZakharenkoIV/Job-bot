package ru.example.jobbot.bot.command.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.example.jobbot.bot.AccessLevel;
import ru.example.jobbot.service.LocalizationService;
import ru.example.jobbot.service.TelegramMessageService;
import ru.example.jobbot.service.cache.CacheService;

@Component
public class RefreshTelegramCommandHandler extends AbstractTelegramCommandHandler {
    private final LocalizationService l10nService;
    private final CacheService cacheService;

    protected RefreshTelegramCommandHandler(CacheService cacheService,
                                            TelegramMessageService messageService,
                                            LocalizationService l10nService) {
        super(
                "/refresh",
                AccessLevel.PUBLIC,
                cacheService,
                messageService);
        this.l10nService = l10nService;
        this.cacheService = cacheService;
    }

    @Override
    SendMessage createSendMessage(Update update) {
        cacheService.deleteChatByTelegramId(update.getMessage().getChatId());
        String languageCode = update.getMessage().getFrom().getLanguageCode();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        sendMessage.setText(l10nService.getLocalizedMessage("refresh_command_success_text", languageCode));
        return sendMessage;
    }

    @Override
    public String getDescription(String languageCode) {
        return l10nService.getLocalizedMessage("refresh_command", languageCode);
    }
}
