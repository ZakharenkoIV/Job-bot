package ru.example.jobbot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.example.jobbot.bot.TelegramBot;
import ru.example.jobbot.service.cache.CacheService;

import java.util.Optional;

@Slf4j
@Service
public class TelegramMessageService {
    private final CacheService cacheService;

    public TelegramMessageService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public void sendMessage(TelegramBot bot, Long telegramUserId, String accessLevel, String commandName, SendMessage sendMessage) {
        try {
            Integer messageId = bot.execute(sendMessage).getMessageId();
            cacheService.putSentMessageId(telegramUserId, accessLevel, commandName, messageId);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке телеграм-сообщения: {}", e.getMessage(), e);
        }
    }

    public void deleteMessage(TelegramBot bot, Long chatId, Optional<Integer> messageId) {
        if (messageId.isPresent()) {
            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(chatId);
            deleteMessage.setMessageId(messageId.get());
            try {
                bot.execute(deleteMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendEditMessage(TelegramBot bot, EditMessageText editMessage) {
        try {
            bot.execute(editMessage);
        } catch (TelegramApiException e) {
            log.debug("Ошибка при отправке сообщения пользователю о регистрации", e);
        }
    }
}
