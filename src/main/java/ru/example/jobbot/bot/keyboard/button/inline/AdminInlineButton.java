package ru.example.jobbot.bot.keyboard.button.inline;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.example.jobbot.bot.keyboard.button.Button;
import ru.example.jobbot.entity.TelegramUser;
import ru.example.jobbot.service.LocalizationService;

@Component
public class AdminInlineButton implements Button {
    private final String callbackData;
    private final LocalizationService l10nService;


    public AdminInlineButton(LocalizationService l10nService) {
        this.callbackData = "button_contact_with_admin";
        this.l10nService = l10nService;
    }

    @Override
    public String getButtonText(String languageCode) {
        return l10nService.getLocalizedMessage("admin_contact_button", languageCode);
    }

    @Override
    public String getCallBackText() {
        return callbackData;
    }

    public InlineKeyboardButton getInlineKeyboardButton(TelegramUser admin, String languageCode) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(this.getButtonText(languageCode));
        button.setCallbackData(callbackData);
        button.setUrl("https://t.me/" + admin.getUserName());
        return button;
    }
}
