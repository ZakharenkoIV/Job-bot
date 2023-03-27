package ru.example.jobbot.bot.keyboard.button.inline;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.example.jobbot.bot.keyboard.button.Button;

@Component
public class RegInlineButton implements Button {
    private final String buttonText;
    private final String callbackData;

    public RegInlineButton() {
        this.buttonText = "Запросить доступ";
        this.callbackData = "button_reg";
    }

    @Override
    public String getButtonText() {
        return buttonText;
    }

    @Override
    public String getCallBackText() {
        return callbackData;
    }

    public InlineKeyboardButton getInlineKeyboardButton() {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(buttonText);
        button.setCallbackData(callbackData);
        return button;
    }
}
