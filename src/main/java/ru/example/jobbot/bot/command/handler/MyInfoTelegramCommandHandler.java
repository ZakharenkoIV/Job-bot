package ru.example.jobbot.bot.command.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.example.jobbot.bot.AccessLevel;
import ru.example.jobbot.service.LocalizationService;
import ru.example.jobbot.service.TelegramMessageService;
import ru.example.jobbot.service.cache.CacheService;

import java.util.StringJoiner;

@Component
public class MyInfoTelegramCommandHandler extends AbstractTelegramCommandHandler {

    private final LocalizationService l10nService;

    public MyInfoTelegramCommandHandler(CacheService cacheService,
                                        TelegramMessageService messageService,
                                        LocalizationService l10nService) {
        super(
                "/my_info",
                AccessLevel.PRIVATE,
                cacheService,
                messageService
        );
        this.l10nService = l10nService;
    }

    @Override
    SendMessage createSendMessage(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        sendMessage.setText(getInfoAboutUser(update));
        return sendMessage;
    }

    private String getInfoAboutUser(Update update) {
        Message message = update.getMessage();
        String languageCode = message.getFrom().getLanguageCode();
        StringJoiner infoAboutUser = new StringJoiner(System.lineSeparator());
        infoAboutUser.add(l10nService.getLocalizedMessage("telegramId_text", languageCode) + " = " + message.getFrom().getId());
        infoAboutUser.add(l10nService.getLocalizedMessage("nickname_text", languageCode) + " = " + message.getFrom().getUserName());
        infoAboutUser.add(l10nService.getLocalizedMessage("firstname_text", languageCode) + " = " + message.getFrom().getFirstName());
        infoAboutUser.add(l10nService.getLocalizedMessage("lastname_text", languageCode) + " = " + message.getFrom().getLastName());
        infoAboutUser.add(l10nService.getLocalizedMessage("languageDigit_text", languageCode) + " = " + languageCode);
        return infoAboutUser.toString();
    }

    @Override
    public String getCommandName() {
        return super.getCommandName();
    }

    @Override
    public String getDescription(String languageCode) {
        return l10nService.getLocalizedMessage("myInfo_command", languageCode);
    }

    @Override
    public String getAccessLevel() {
        return super.getAccessLevel();
    }
}
