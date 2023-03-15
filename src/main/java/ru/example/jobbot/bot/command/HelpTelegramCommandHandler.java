package ru.example.jobbot.bot.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class HelpTelegramCommandHandler extends AbstractTelegramCommandHandler {

    @Override
    SendMessage createSendMessage(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Вызвана команда /help");
        return sendMessage;
    }

    @Override
    public String getCommandName() {
        return "/help";
    }
}