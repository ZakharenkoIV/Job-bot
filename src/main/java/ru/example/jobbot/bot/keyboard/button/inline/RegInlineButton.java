package ru.example.jobbot.bot.keyboard.button.inline;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.example.jobbot.bot.keyboard.button.Button;
import ru.example.jobbot.service.LocalizationService;

@Component
public class RegInlineButton implements Button {
    private final LocalizationService l10nService;
    private final String callbackData;

    public RegInlineButton(LocalizationService l10nService) {
        this.l10nService = l10nService;
        this.callbackData = "button_reg";
    }

    @Override
    public String getButtonText(String languageCode) {
        return l10nService.getLocalizedMessage("reg_button", languageCode);
    }

    @Override
    public String getCallBackText() {
        return callbackData;
    }

    public InlineKeyboardButton getInlineKeyboardButton(String languageCode) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(this.getButtonText(languageCode));
        button.setCallbackData(callbackData);
        return button;
    }
}
