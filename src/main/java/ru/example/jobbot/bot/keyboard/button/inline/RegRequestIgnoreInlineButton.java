package ru.example.jobbot.bot.keyboard.button.inline;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.example.jobbot.bot.keyboard.button.Button;

@Component
public class RegRequestIgnoreInlineButton implements Button {
    private final String buttonText;
    private final String callbackData;

    public RegRequestIgnoreInlineButton() {
        this.buttonText = "Отклонить";
        this.callbackData = "button_reg_ignore";
    }

    @Override
    public String getButtonText() {
        return buttonText;
    }

    @Override
    public String getCallBackText() {
        return callbackData;
    }

    public InlineKeyboardButton getInlineKeyboardButton(Long telegramUserId) {
        InlineKeyboardButton ignoreButton = new InlineKeyboardButton();
        ignoreButton.setText(buttonText);
        JSONObject data = new JSONObject();
        data.put("command_name", callbackData);
        data.put("user_id", telegramUserId);
        ignoreButton.setCallbackData(data.toString());
        return ignoreButton;
    }
}
