package ru.example.jobbot.bot.keyboard.button.handler.reg;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.example.jobbot.bot.AccessLevel;
import ru.example.jobbot.bot.Constants;
import ru.example.jobbot.bot.TelegramBot;
import ru.example.jobbot.bot.command.handler.StartTelegramCommandHandler;
import ru.example.jobbot.bot.keyboard.button.handler.ButtonHandler;
import ru.example.jobbot.entity.TelegramUser;
import ru.example.jobbot.service.LocalizationService;
import ru.example.jobbot.service.TelegramMessageService;
import ru.example.jobbot.service.UserService;
import ru.example.jobbot.service.cache.CacheService;

import java.util.concurrent.*;

@Slf4j
@Component
public class RegRequestAddInlineButtonHandler implements ButtonHandler {

    private final String accessLevel;
    private final String buttonHandlerName;
    private final String bindingCommandName;
    private final UserService userService;
    private final CacheService cacheService;
    private final TelegramMessageService messageService;
    private final LocalizationService l10nService;

    public RegRequestAddInlineButtonHandler(UserService userService,
                                            CacheService cacheService,
                                            TelegramMessageService messageService,
                                            StartTelegramCommandHandler startCommand,
                                            LocalizationService l10nService) {
        this.buttonHandlerName = "button_reg_add";
        this.accessLevel = AccessLevel.ADMIN;
        this.userService = userService;
        this.cacheService = cacheService;
        this.messageService = messageService;
        this.bindingCommandName = startCommand.getCommandName();
        this.l10nService = l10nService;
    }

    @Override
    public String getAccessLevel() {
        return accessLevel;
    }

    @Override
    public String getButtonHandlerName() {
        return buttonHandlerName;
    }

    @Override
    public void handleButtonPress(TelegramBot bot, Update update, TelegramUser user) {
        Long newTelegramUserId = Long.parseLong(new JSONObject(update.getCallbackQuery().getData()).get("user_id").toString());
        deleteRequestMessageFromAdmin(bot, newTelegramUserId);
        TelegramUser newUser = saveNewUser(newTelegramUserId);
        deletePreviousMessageFromUser(bot, newUser.getTelegramId());
        sendSuccessMessage(bot, newUser);
    }

    private void sendSuccessMessage(TelegramBot bot, TelegramUser newUser) {
        String accessGrantedText = l10nService.getLocalizedMessage("reg_handler_access_granted_success_text", newUser.getLanguageCode());
        sendMessageToUser(bot, newUser.getTelegramId(), accessGrantedText);

        String updateCommandsText = l10nService.getLocalizedMessage(
                "reg_handler_update_commands_send_any_message_text", newUser.getLanguageCode());
        sendDelayedMessageToUser(bot, newUser.getTelegramId(), updateCommandsText);
    }

    private TelegramUser saveNewUser(Long newTelegramUserId) {
        TelegramUser newUser = cacheService.removeTelegramUserFromRegistrationMap(newTelegramUserId);
        newUser.setAccessLevel(AccessLevel.PUBLIC + ", " + AccessLevel.PRIVATE);
        cacheService.removeChatId(newTelegramUserId);
        userService.saveUser(newUser);
        return newUser;
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

    private void sendMessageToUser(TelegramBot bot, Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setText(text);
        message.setChatId(chatId);
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.debug("Ошибка при отправке сообщения пользователю", e);
        }
    }

    private void sendDelayedMessageToUser(TelegramBot bot, Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setText(text);
        message.setChatId(chatId);
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        try {

            ScheduledFuture<?> future = executor.schedule(() -> {
                try {
                    bot.execute(message);
                } catch (TelegramApiException e) {
                    log.debug("Ошибка при отправке задержанного сообщения пользователю", e);
                }
            }, 1, TimeUnit.SECONDS);

            future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Ошибка при выполнении задачи", e);
        } finally {
            executor.shutdown();
        }
    }

}