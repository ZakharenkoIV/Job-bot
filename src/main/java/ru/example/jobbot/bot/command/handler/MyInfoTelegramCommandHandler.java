package ru.example.jobbot.bot.command.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.example.jobbot.bot.AccessLevel;
import ru.example.jobbot.service.TelegramMessageService;
import ru.example.jobbot.service.cache.CacheService;

import java.util.StringJoiner;

@Component
public class MyInfoTelegramCommandHandler extends AbstractTelegramCommandHandler {

    public MyInfoTelegramCommandHandler(CacheService cacheService, TelegramMessageService messageService) {
        super("/my_info", "Информация о себе", AccessLevel.PRIVATE, cacheService, messageService);
    }

    @Override
    SendMessage createSendMessage(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        sendMessage.setText(getInfoAboutUser(update));
        return sendMessage;
    }

    private String getInfoAboutUser(Update update) {
        Message message = update.getMessage();
        StringJoiner infoAboutUser = new StringJoiner(System.lineSeparator());
        infoAboutUser.add("Telegram ID = " + message.getFrom().getId());
        infoAboutUser.add("Ник = " + message.getFrom().getUserName());
        infoAboutUser.add("Имя = " + message.getFrom().getFirstName());
        infoAboutUser.add("Фамилия = " + message.getFrom().getLastName());
        infoAboutUser.add("Код языка = " + message.getFrom().getLanguageCode());
        return infoAboutUser.toString();
    }

    @Override
    public String getCommandName() {
        return super.getCommandName();
    }

    @Override
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    public String getAccessLevel() {
        return super.getAccessLevel();
    }
}
