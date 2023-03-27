package ru.example.jobbot.service;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LocalizationService {

    private final MessageSource messageSource;

    public LocalizationService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getLocalizedMessage(String key, String languageCode) {
        Locale currentLocale = getCurrentLocale(languageCode);
        return messageSource.getMessage(key, null, currentLocale);
    }

    private Locale getCurrentLocale(String languageCode) {
        if (languageCode.equals("ru")) {
            return new Locale("ru", "RU");
        } else if (languageCode.equals("en")) {
            return Locale.ENGLISH;
        }
        return LocaleContextHolder.getLocale();
    }
}
