package ru.example.jobbot.bot.update;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.example.jobbot.bot.AccessLevel;
import ru.example.jobbot.bot.TelegramBot;
import ru.example.jobbot.bot.TelegramCommandFilter;
import ru.example.jobbot.entity.TelegramUser;
import ru.example.jobbot.service.UserService;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class TelegramUpdateHandler {

    private final TelegramCommandFilter commandFilter;
    private final UserService userService;

    public void handleUpdate(TelegramBot bot, Update update) {
        TelegramUser user = getTelegramUser(update);
        commandFilter.handleCommandEvent(bot, update, user);
    }

    private TelegramUser getTelegramUser(Update update) {
        if (update.hasCallbackQuery()) {
            return getTelegramUserFromMessage(update.getCallbackQuery().getFrom(), update.getCallbackQuery().getMessage().getChatId());
        } else if (hasMessageFromUser(update)) {
            return getTelegramUserFromMessage(update.getMessage().getFrom(), update.getMessage().getChatId());
        } else {
            TelegramUser undefinedUser = new TelegramUser();
            undefinedUser.setAccessLevel(AccessLevel.BLOCKED);
            return undefinedUser;
        }
    }

    private TelegramUser getTelegramUserFromMessage(User telegramUser, Long chatId) {
        long telegramUserId = telegramUser.getId();
        Optional<TelegramUser> foundUser = userService.getUser(telegramUserId);
        if (foundUser.isEmpty()) {
            TelegramUser visitor = new TelegramUser();
            visitor.setTelegramId(telegramUserId);
            visitor.setUserName(telegramUser.getUserName());
            visitor.setFirstName(telegramUser.getFirstName());
            visitor.setLastName(telegramUser.getLastName());
            visitor.setLanguageCode(telegramUser.getLanguageCode());
            visitor.setAccessLevel(AccessLevel.PUBLIC + ", " + AccessLevel.REG);
            visitor.setTelegramChatId(chatId);
            return visitor;
        } else {
            return foundUser.get();
        }
    }

    private boolean hasMessageFromUser(Update update) {
        return update.hasMessage() && update.getMessage().hasText() && update.getMessage().getFrom() != null;
    }
}