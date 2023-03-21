package ru.example.jobbot.bot.command.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.StringJoiner;

@Component
public class MyInfoTelegramCommandHandler extends AbstractTelegramCommandHandler {
    @Override
    SendMessage createSendMessage(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        sendMessage.setText(getInfoAboutUser(update));
        return sendMessage;
    }

    @Override
    public String getCommandName() {
        return "/my_info";
    }

    @Override
    public String getDescription() {
        return "Информация о себе";
    }

    @Override
    public String getScope() {
        return "private";
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
}
