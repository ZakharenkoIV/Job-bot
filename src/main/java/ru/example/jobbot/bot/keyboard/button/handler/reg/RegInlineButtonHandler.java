package ru.example.jobbot.bot.keyboard.button.handler.reg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.example.jobbot.bot.AccessLevel;
import ru.example.jobbot.bot.Constants;
import ru.example.jobbot.bot.TelegramBot;
import ru.example.jobbot.bot.command.handler.StartTelegramCommandHandler;
import ru.example.jobbot.bot.keyboard.RegRequestInlineKeyboardMarkup;
import ru.example.jobbot.bot.keyboard.button.handler.ButtonHandler;
import ru.example.jobbot.entity.TelegramUser;
import ru.example.jobbot.service.LocalizationService;
import ru.example.jobbot.service.TelegramMessageService;
import ru.example.jobbot.service.cache.CacheService;

import java.util.StringJoiner;

@Slf4j
@Component
public class RegInlineButtonHandler implements ButtonHandler {

    private final String accessLevel;
    private final String buttonHandlerName;
    private final RegRequestInlineKeyboardMarkup keyboardMarkup;
    private final CacheService cacheService;
    private final String bindingCommandName;
    private final TelegramMessageService messageService;
    private final LocalizationService l10nService;

    public RegInlineButtonHandler(RegRequestInlineKeyboardMarkup keyboardMarkup,
                                  CacheService cacheService,
                                  StartTelegramCommandHandler startCommand,
                                  TelegramMessageService messageService,
                                  LocalizationService l10nService) {
        this.keyboardMarkup = keyboardMarkup;
        this.buttonHandlerName = "button_reg";
        this.accessLevel = AccessLevel.REG;
        this.cacheService = cacheService;
        this.bindingCommandName = startCommand.getCommandName();
        this.messageService = messageService;
        this.l10nService = l10nService;
    }

    @Override
    public String getButtonHandlerName() {
        return buttonHandlerName;
    }

    @Override
    public void handleButtonPress(TelegramBot bot, Update update, TelegramUser user) {
        cacheService.addRegistrationMap(user);
        editMessageFromUser(bot, user);
        sendMessageFromAdmin(bot, user);
    }

    private void sendMessageFromAdmin(TelegramBot bot, TelegramUser user) {
        SendMessage adminMessage = new SendMessage();
        StringJoiner textMessage = new StringJoiner(System.lineSeparator());

        textMessage.add(l10nService.getLocalizedMessage("reg_handler_new_access_request_text", "default") + ": ");
        textMessage.add(l10nService.getLocalizedMessage("nickname_text", "default") + ": " + user.getUserName());
        textMessage.add(l10nService.getLocalizedMessage("firstname_text", "default") + ": " + user.getFirstName());
        textMessage.add(l10nService.getLocalizedMessage("lastname_text", "default") + ": " + user.getLastName());
        textMessage.add(l10nService.getLocalizedMessage("telegramId_text", "default") + ": " + user.getTelegramId());
        textMessage.add(l10nService.getLocalizedMessage("chatId_text", "default") + "Chat ID: " + user.getTelegramChatId());
        textMessage.add(l10nService.getLocalizedMessage("languageDigit_text", "default") + ": " + user.getLanguageCode());
        adminMessage.setText(textMessage.toString());
        adminMessage.setChatId(Constants.ADMIN_ID);
        adminMessage.setReplyMarkup(keyboardMarkup.getInlineKeyboardMarkup(user));
        messageService.sendMessage(bot, user.getTelegramId(), AccessLevel.ADMIN, bindingCommandName, adminMessage);
    }

    private void editMessageFromUser(TelegramBot bot, TelegramUser user) {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(user.getTelegramChatId());
        editMessage.setMessageId(cacheService.getSentMessageId(user.getTelegramChatId(), user.getAccessLevel(), bindingCommandName).orElseThrow());
        editMessage.setText(
                l10nService.getLocalizedMessage("reg_handler_request_sent_to_admin_text", user.getLanguageCode())
        );
        messageService.sendEditMessage(bot, editMessage);
    }

    @Override
    public String getAccessLevel() {
        return accessLevel;
    }
}
