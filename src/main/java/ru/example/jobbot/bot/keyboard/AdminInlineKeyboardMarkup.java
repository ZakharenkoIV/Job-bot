package ru.example.jobbot.bot.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.example.jobbot.bot.keyboard.button.inline.AdminInlineButton;
import ru.example.jobbot.entity.TelegramUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class AdminInlineKeyboardMarkup {
    private final AdminInlineButton adminButton;

    public AdminInlineKeyboardMarkup(AdminInlineButton adminButton) {
        this.adminButton = adminButton;
    }

    public InlineKeyboardMarkup getInlineKeyboardMarkup(TelegramUser user) {
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(adminButton.getInlineKeyboardButton(user));
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(Collections.singletonList(row));
        return markup;
    }
}
