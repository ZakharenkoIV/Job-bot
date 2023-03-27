package ru.example.jobbot.bot.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.example.jobbot.bot.keyboard.button.inline.RegInlineButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class RegInlineKeyboardMarkup {
    private final RegInlineButton regButton;

    public RegInlineKeyboardMarkup(RegInlineButton regButton) {
        this.regButton = regButton;
    }

    public InlineKeyboardMarkup getInlineKeyboardMarkup(String languageCode) {
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(regButton.getInlineKeyboardButton(languageCode));
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(Collections.singletonList(row));
        return markup;
    }
}
