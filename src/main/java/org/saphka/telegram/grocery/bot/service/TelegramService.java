package org.saphka.telegram.grocery.bot.service;

import org.saphka.telegram.grocery.bot.config.BotProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class TelegramService extends DefaultAbsSender {
    private static final Logger log = LoggerFactory.getLogger(TelegramService.class);

    public TelegramService(BotProperties properties) {
        super(new DefaultBotOptions(), properties.token());
    }

    public void processUpdate(Update update) {
        var msg = new SendMessage();
        msg.setChatId(update.getMessage().getChatId());
        msg.setText("Hello, world!");
        this.sendApiMethodAsync(msg).exceptionally(ex -> {
            log.error("Error processing update {}", update, ex);
            return null;
        });
    }
}
