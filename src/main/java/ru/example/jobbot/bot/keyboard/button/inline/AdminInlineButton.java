package ru.example.jobbot.bot.keyboard.button.inline;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.example.jobbot.bot.keyboard.button.Button;
import ru.example.jobbot.entity.TelegramUser;

@Component
public class AdminInlineButton implements Button {
    private final String buttonText;
    private final String callbackData;

    public AdminInlineButton() {
        this.buttonText = "Открыть чат";
        this.callbackData = "button_contact_with_admin";
    }

    @Override
    public String getButtonText() {
        return buttonText;
    }

    @Override
    public String getCallBackText() {
        return callbackData;
    }

    public InlineKeyboardButton getInlineKeyboardButton(TelegramUser user) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(buttonText);
        button.setCallbackData(callbackData);
        button.setUrl("https://t.me/" + user.getUserName());
        return button;
    }
}
