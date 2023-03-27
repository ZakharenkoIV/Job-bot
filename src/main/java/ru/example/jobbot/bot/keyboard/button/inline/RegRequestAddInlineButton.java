package ru.example.jobbot.bot.keyboard.button.inline;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.example.jobbot.bot.keyboard.button.Button;

@Component
public class RegRequestAddInlineButton implements Button {
    private final String buttonText;
    private final String callbackData;

    public RegRequestAddInlineButton() {
        this.buttonText = "Одобрить";
        this.callbackData = "button_reg_add";
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
        InlineKeyboardButton addButton = new InlineKeyboardButton();
        addButton.setText(buttonText);
        JSONObject data = new JSONObject();
        data.put("command_name", callbackData);
        data.put("user_id", telegramUserId);
        addButton.setCallbackData(data.toString());
        return addButton;
    }
}
