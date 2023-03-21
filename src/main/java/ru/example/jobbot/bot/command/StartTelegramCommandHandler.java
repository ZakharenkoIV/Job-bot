package ru.example.jobbot.bot.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class StartTelegramCommandHandler extends AbstractTelegramCommandHandler {

    @Override
    SendMessage createSendMessage(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        sendMessage.setText("Привет! Это начало работы с ботом.");
        return sendMessage;
    }

    @Override
    public String getCommandName() {
        return "/start";
    }
}