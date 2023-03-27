package ru.example.jobbot.bot.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.example.jobbot.bot.keyboard.button.inline.RegRequestAddInlineButton;
import ru.example.jobbot.bot.keyboard.button.inline.RegRequestIgnoreInlineButton;
import ru.example.jobbot.entity.TelegramUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class RegRequestInlineKeyboardMarkup {

    private final RegRequestAddInlineButton addButton;
    private final RegRequestIgnoreInlineButton ignoreButton;

    public RegRequestInlineKeyboardMarkup(RegRequestAddInlineButton addButton, RegRequestIgnoreInlineButton ignoreButton) {
        this.addButton = addButton;
        this.ignoreButton = ignoreButton;
    }

    public InlineKeyboardMarkup getInlineKeyboardMarkup(TelegramUser user) {
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(addButton.getInlineKeyboardButton(user));
        row.add(ignoreButton.getInlineKeyboardButton(user));
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(Collections.singletonList(row));
        return markup;
    }
}
