package ru.example.jobbot.bot.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class HelpAbstractTelegramCommandHandler extends AbstractTelegramCommandHandler {

    @Override
    public String getCommandName() {
        return "/help";
    }

    SendMessage createSendMessage(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Вызвана команда /help");
        return sendMessage;
    }
}