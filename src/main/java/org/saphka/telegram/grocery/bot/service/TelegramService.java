package org.saphka.telegram.grocery.bot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class TelegramService {
    public BotApiMethod<?> processUpdate(Update update) {
        var msg = new SendMessage();
        msg.setChatId(update.getMessage().getChatId());
        msg.setText("Hello, world!");
        return msg;
    }
}
