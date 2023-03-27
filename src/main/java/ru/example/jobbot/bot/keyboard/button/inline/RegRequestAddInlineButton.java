package ru.example.jobbot.bot.keyboard.button.inline;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.example.jobbot.bot.keyboard.button.Button;
import ru.example.jobbot.entity.TelegramUser;
import ru.example.jobbot.service.LocalizationService;

@Component
public class RegRequestAddInlineButton implements Button {
    private final LocalizationService l10nService;
    private final String callbackData;

    public RegRequestAddInlineButton(LocalizationService l10nService) {
        this.l10nService = l10nService;
        this.callbackData = "button_reg_add";
    }

    @Override
    public String getButtonText(String languageCode) {
        return l10nService.getLocalizedMessage("regRequestAdd_button", languageCode);
    }

    @Override
    public String getCallBackText() {
        return callbackData;
    }

    public InlineKeyboardButton getInlineKeyboardButton(TelegramUser user) {
        InlineKeyboardButton addButton = new InlineKeyboardButton();
        addButton.setText(this .getButtonText("default"));
        JSONObject data = new JSONObject();
        data.put("command_name", callbackData);
        data.put("user_id", user.getTelegramId());
        addButton.setCallbackData(data.toString());
        return addButton;
    }
}
