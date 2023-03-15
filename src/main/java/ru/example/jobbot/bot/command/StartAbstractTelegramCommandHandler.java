package ru.example.jobbot.bot.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class StartAbstractTelegramCommandHandler extends AbstractTelegramCommandHandler {

    @Override
    public String getCommandName() {
        return "/start";
    }

    SendMessage createSendMessage(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Привет! Это начало работы с ботом.");
        return sendMessage;
    }
}