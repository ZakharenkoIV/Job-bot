package ru.example.jobbot.bot.command.handler;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.example.jobbot.bot.TelegramBot;

@Slf4j
public abstract class AbstractTelegramCommandHandler implements TelegramCommandHandler {

    @Override
    public void handleCommand(TelegramBot bot, Update update) {
        long chatId = update.getMessage().getChatId();
        SendMessage sendMessage = createSendMessage(update);
        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Ошибка при обработке команды: {}, chatId: {}", e.getMessage(), chatId, e);
        }
    }

    abstract SendMessage createSendMessage(Update update);
}
