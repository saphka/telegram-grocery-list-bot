package org.saphka.telegram.grocery.bot.service;

import org.saphka.telegram.grocery.bot.config.BotProperties;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.GetMe;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class TelegramApiService extends TelegramWebhookBot {

    private final BotProperties properties;

    public TelegramApiService(BotProperties properties) {
        super(properties.token());
        this.properties = properties;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return new GetMe();
    }

    @Override
    public String getBotPath() {
        return properties.path();
    }

    @Override
    public String getBotUsername() {
        return properties.username();
    }

    @Override
    public void setWebhook(SetWebhook setWebhook) {
        //do nothing
    }
}
