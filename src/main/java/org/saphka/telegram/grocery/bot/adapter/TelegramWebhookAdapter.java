package org.saphka.telegram.grocery.bot.adapter;

import org.saphka.telegram.grocery.bot.config.BotProperties;
import org.saphka.telegram.grocery.bot.service.TelegramService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Profile("prod")
public class TelegramWebhookAdapter extends TelegramWebhookBot {

    private final BotProperties properties;
    private final TelegramService service;

    public TelegramWebhookAdapter(BotProperties properties, TelegramService service) {
        super(properties.token());
        this.properties = properties;
        this.service = service;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return service.processUpdate(update);
    }

    @Override
    public String getBotPath() {
        return properties.path();
    }

    @Override
    public String getBotUsername() {
        return properties.username();
    }
}
