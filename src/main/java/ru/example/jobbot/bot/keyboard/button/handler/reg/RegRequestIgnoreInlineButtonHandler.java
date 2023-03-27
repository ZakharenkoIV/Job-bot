package ru.example.jobbot.bot.keyboard.button.handler.reg;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.example.jobbot.bot.AccessLevel;
import ru.example.jobbot.bot.Constants;
import ru.example.jobbot.bot.TelegramBot;
import ru.example.jobbot.bot.command.handler.StartTelegramCommandHandler;
import ru.example.jobbot.bot.keyboard.AdminInlineKeyboardMarkup;
import ru.example.jobbot.bot.keyboard.button.handler.ButtonHandler;
import ru.example.jobbot.entity.TelegramUser;
import ru.example.jobbot.service.TelegramMessageService;
import ru.example.jobbot.service.cache.CacheService;

@Slf4j
@Component
public class RegRequestIgnoreInlineButtonHandler implements ButtonHandler {

    private final String accessLevel;
    private final String buttonHandlerName;
    private final String bindingCommandName;
    private final TelegramMessageService messageService;
    private final CacheService cacheService;
    private final AdminInlineKeyboardMarkup adminMarkup;

    public RegRequestIgnoreInlineButtonHandler(TelegramMessageService messageService, CacheService cacheService, StartTelegramCommandHandler startCommand, AdminInlineKeyboardMarkup adminMarkup) {
        this.messageService = messageService;
        this.cacheService = cacheService;
        this.adminMarkup = adminMarkup;
        this.buttonHandlerName = "button_reg_ignore";
        this.accessLevel = AccessLevel.ADMIN;
        this.bindingCommandName = startCommand.getCommandName();
    }

    @Override
    public void handleButtonPress(TelegramBot bot, Update update, TelegramUser user) {
        Long newTelegramUserId = Long.parseLong(new JSONObject(update.getCallbackQuery().getData()).get("user_id").toString());
        deletePreviousMessageFromUser(bot, newTelegramUserId);
        sendIgnoreMessage(bot, user, newTelegramUserId);
        deleteRequestMessageFromAdmin(bot, newTelegramUserId);
    }

    @Override
    public String getButtonHandlerName() {
        return buttonHandlerName;
    }

    @Override
    public String getAccessLevel() {
        return accessLevel;
    }

    private SendMessage createSendMessage(TelegramUser user, Long newTelegramUserId, String accessFailedText) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(newTelegramUserId);
        sendMessage.setText(accessFailedText);
        sendMessage.setReplyMarkup(adminMarkup.getInlineKeyboardMarkup(user));
        return sendMessage;
    }

    private void deletePreviousMessageFromUser(TelegramBot bot, Long newTelegramUserId) {
        messageService.deleteMessage(
                bot,
                newTelegramUserId,
                cacheService.getSentMessageId(newTelegramUserId, AccessLevel.PUBLIC, bindingCommandName));
    }

    private void deleteRequestMessageFromAdmin(TelegramBot bot, Long newTelegramUserId) {
        messageService.deleteMessage(
                bot,
                Long.parseLong(Constants.ADMIN_ID),
                cacheService.getSentMessageId(newTelegramUserId, AccessLevel.ADMIN, bindingCommandName)
        );
    }

    private void sendIgnoreMessage(TelegramBot bot, TelegramUser user, Long newTelegramUserId) {
        String accessDeniedText = String.format("Доступ отклонён.%nОбратитесь к администратору.");
        createSendMessage(user, newTelegramUserId, accessDeniedText);
        SendMessage sendMessage = createSendMessage(user, newTelegramUserId, accessDeniedText);
        messageService.sendMessage(bot, newTelegramUserId, AccessLevel.PUBLIC, bindingCommandName, sendMessage);
    }
}
