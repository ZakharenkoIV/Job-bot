package ru.example.jobbot.bot.keyboard.button.inline;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.example.jobbot.bot.keyboard.button.Button;
import ru.example.jobbot.entity.TelegramUser;
import ru.example.jobbot.service.LocalizationService;

@Component
public class RegRequestIgnoreInlineButton implements Button {
    private final LocalizationService l10nService;
    private final String callbackData;

    public RegRequestIgnoreInlineButton(LocalizationService l10nService) {
        this.l10nService = l10nService;
        this.callbackData = "button_reg_ignore";
    }

    @Override
    public String getButtonText(String languageCode) {
        return l10nService.getLocalizedMessage("regRequestIgnore_button", languageCode);
    }

    @Override
    public String getCallBackText() {
        return callbackData;
    }

    public InlineKeyboardButton getInlineKeyboardButton(TelegramUser user) {
        InlineKeyboardButton ignoreButton = new InlineKeyboardButton();
        ignoreButton.setText(this.getButtonText("default"));
        JSONObject data = new JSONObject();
        data.put("command_name", callbackData);
        data.put("user_id", user.getTelegramId());
        ignoreButton.setCallbackData(data.toString());
        return ignoreButton;
    }
}
